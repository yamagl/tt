package com.abchina.tt.service;

import com.abchina.tt.Environment;
import com.abchina.tt.GeoCode;
import com.abchina.tt.GeoResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StaticMapService {
    private static Environment environment = Environment.getInstance();
    private static final String size = "1024*1024";
    private static Logger logger = LoggerFactory.getLogger(StaticMapService.class);

    public static void markStaticMap(List<String> addressList) throws IOException, URISyntaxException {
        Environment environment = Environment.getInstance();
        HttpClient httpClient = environment.getHttpClient();
//        location=116.481485,39.990464&zoom=10&size=750*300&markers=mid,,A:116.481485,39.990464&key=<用户的key>
//        String centerLocation = GeoService.queryGeo("上海市")
//                .orElse(new GeoResponse("121.342956,31.160698"))
//                .getGeocodes().get(0).getLocation();
        String centerLocation = "121.342956,31.160698";

        HttpGet mapGet = new HttpGet(uriBuilder(addressList.stream()
                .map(a -> GeoService.queryGeo(a).orElse(null))
                .filter(Objects::nonNull)
                .map(r -> r.getGeocodes().stream().map(GeoCode::getLocation))
                .reduce(Stream::concat)
                .orElse(Stream.empty())
                .collect(Collectors.toList()), centerLocation));
        HttpResponse response = httpClient.execute(mapGet);
        byte[] data = IOUtils.toByteArray(response.getEntity().getContent());
        if (new String(Arrays.copyOfRange(data, 1, 4)).equals("PNG")) {
            try (OutputStream outputStream = new FileOutputStream(new File("map/" + System.currentTimeMillis() + ".png"))) {
                IOUtils.writeChunked(data, outputStream);
                logger.debug("download map");
            }
        } else {
            logger.warn(new String(data));
        }
        HttpClientUtils.closeQuietly(response);
    }

    static URI uriBuilder(List<String> markers, String center) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder()
                .setScheme("https")
                .setHost(environment.getHost())
                .setPath("/v3/staticmap")
                .setParameter("size", size)
                .setParameter("scale", "2")
                .setParameter("zoom", "10")
                .setParameter("location", center)
                .setParameter("key", environment.getKey());

        if (markers != null && !markers.isEmpty()) {
            uriBuilder.setParameter("markers", "small,0xFF0000,A:"
                    + markers.stream()
                    .reduce((a, b) -> a + ";" + b).orElse(""));
        }
        URI uri = uriBuilder.build();
        logger.debug(uri.toString());
        return uri;
    }

}
