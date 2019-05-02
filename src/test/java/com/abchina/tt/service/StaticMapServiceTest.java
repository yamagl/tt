package com.abchina.tt.service;


import com.abchina.tt.Environment;
import com.abchina.tt.GeoCode;
import com.abchina.tt.GeoResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StaticMapServiceTest {

    @Test
    public void staticMap() throws IOException, URISyntaxException {
        String size = "1024*1024";
        Environment environment = Environment.getInstance();
        HttpClient httpClient = environment.getHttpClient();
//        location=116.481485,39.990464&zoom=10&size=750*300&markers=mid,,A:116.481485,39.990464&key=<用户的key>
        String location = "121.342956,31.160698";
        System.out.println("location = " + location);
        URI mapUri = new URIBuilder()
                .setScheme("https")
                .setHost(environment.getHost())
                .setPath("/v3/staticmap")
                .setParameter("size", size)
//                .setParameter("scale", "2")
//                .setParameter("markers", )
                .setParameter("zoom", "10")
                .setParameter("location", location)
                .setParameter("key", environment.getKey())
                .build();
        HttpGet mapGet = new HttpGet(mapUri);
        System.out.println(mapGet.getURI().toString());
        HttpResponse response = httpClient.execute(mapGet);
        byte[] data = IOUtils.toByteArray(response.getEntity().getContent());
        if (new String(Arrays.copyOfRange(data, 1, 4)).equals("PNG")) {
            IOUtils.write(data, new FileOutputStream(new File("map/" + System.currentTimeMillis() + ".png")));
        } else {
            System.out.println(new String(data));
        }
    }

    @Test
    void uriBuilder() throws URISyntaxException {
        String[] address = new String[]{"闵行区七宝镇新镇路1060弄12号604室", "上海市闵行区七宝镇青年路190号601室", "七宝镇青年路267弄7号601室"};
        List<String> locations = Arrays.stream(address)
                .map(GeoService::queryGeo)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(GeoResponse::getGeocodes)
                .map(codes -> codes.stream().map(GeoCode::getLocation))
                .reduce(Stream::concat)
                .orElse(Stream.empty())
                .collect(Collectors.toList());
        System.out.println(StaticMapService.uriBuilder(locations, "121.342956,31.160698"));
    }


    @Test
    void markStaticMap() throws IOException, URISyntaxException {
        String[] address = new String[]{"闵行区七宝镇新镇路1060弄12号604室", "上海市闵行区七宝镇青年路190号601室", "七宝镇青年路267弄7号601室"};
        StaticMapService.markStaticMap(Arrays.asList(address));
    }

}
