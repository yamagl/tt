package com.abchina.tt;

import java.util.ArrayList;
import java.util.List;

public class GeoResponse {
    private String status;
    private String info;
    private String infocode;
    private String count;
    private List<GeoCode> geocodes;

    public GeoResponse() {}

    public GeoResponse(String location) {
        this.geocodes =  new ArrayList<>();
        this.geocodes.add(new GeoCode(){{setLocation(location);}});
    }

    @Override
    public String toString() {
        return "GeoResponse{" +
                "status='" + status + '\'' +
                ", info='" + info + '\'' +
                ", infocode='" + infocode + '\'' +
                ", count='" + count + '\'' +
                ", geocodes=" + geocodes +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<GeoCode> getGeocodes() {
        return geocodes;
    }

    public void setGeocodes(List<GeoCode> geocodes) {
        this.geocodes = geocodes;
    }
}
