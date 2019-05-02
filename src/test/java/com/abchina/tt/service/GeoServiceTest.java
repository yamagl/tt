package com.abchina.tt.service;


import com.abchina.tt.GeoResponse;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

public class GeoServiceTest {

    @Test
    public void queryAmap() throws IOException, URISyntaxException {
        GeoService geoService = new GeoService();
        geoService.queryGeo("上海市七莘路3333号");
    }

    @Test
    public void parseJSON() {
        String json = "{\"status\":\"1\",\"info\":\"OK\",\"infocode\":\"10000\",\"count\":\"1\",\"geocodes\":[{\"formatted_address\":\"北京市朝阳区阜通东大街|6号\",\"country\":\"中国\",\"province\":\"北京市\",\"citycode\":\"010\",\"city\":\"北京市\",\"district\":\"朝阳区\",\"township\":[],\"neighborhood\":{\"name\":[],\"type\":[]},\"building\":{\"name\":[],\"type\":[]},\"adcode\":\"110105\",\"street\":\"阜通东大街\",\"number\":\"6号\",\"location\":\"116.483038,39.990633\",\"level\":\"门牌号\"}]}";
        GeoResponse geoResponse = JSON.parseObject(json, GeoResponse.class);
    }

}
