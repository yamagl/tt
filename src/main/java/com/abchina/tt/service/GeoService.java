package com.abchina.tt.service;

import com.abchina.tt.CountDown;
import com.abchina.tt.GeoResponse;
import com.abchina.tt.Environment;
import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class GeoService {

    private static Logger logger = LoggerFactory.getLogger(GeoService.class);
    private static Environment environment = Environment.getInstance();


    public static Optional<GeoResponse> queryGeo(String address) {
        HttpClient httpClient = environment.getHttpClient();
        URI geoUri;
        try {
            geoUri = new URIBuilder()
                    .setScheme("https")
                    .setHost(environment.getHost())
                    .setPath("/v3/geocode/geo")
                    .setParameter("address", address)
                    .setParameter("output", "JSON")
                    .setParameter("key", environment.getKey())
                    .build();
        } catch (URISyntaxException e) {
            logger.error("Error building URI", e);
            return Optional.empty();
        }
        HttpGet geoGet = new HttpGet(geoUri);
        logger.debug(geoGet.getURI().toString());

        HttpResponse httpResponse = null;
        try {
            CountDown httpCountDown = new CountDown( 300000, "block on " + geoGet.getURI().toString());
            new Thread(httpCountDown).start();
            httpResponse = httpClient.execute(geoGet);
            httpCountDown.stop();
            String body = IOUtils.toString(httpResponse.getEntity().getContent(), Charset.forName("UTF-8"));
            GeoResponse geoResponse = JSON.parseObject(body, GeoResponse.class);
            logger.debug(geoResponse.toString());
            if (geoResponse.getStatus().equals("0"))
                return Optional.empty();
            if (geoResponse.getGeocodes() == null || geoResponse.getGeocodes().isEmpty()) {
                logger.debug(String.format("empty location: %s", address));
                String prefix = "上海市";
                if (!address.contains(prefix))
                    return queryGeo(prefix + address);
                return Optional.empty();
            }
            return Optional.of(geoResponse);
        } catch (IOException e) {
            logger.error("Error sending request", e);
            return Optional.empty();
        } finally {
            geoGet.reset();
            HttpClientUtils.closeQuietly(httpResponse);
        }
    }

}
