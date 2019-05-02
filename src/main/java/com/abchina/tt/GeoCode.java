package com.abchina.tt;

import java.util.List;

public class GeoCode {
    private String formatted_address;
    private String country;
    private String province;
    private String citycode;
    private String city;
    private String district;
    private String township;
    private List<AddInfo> neighborhood;
    private List<AddInfo> building;
    private String adcode;
    private String street;
    private String number;
    private String location;
    private String level;

    @Override
    public String toString() {
        return "GeoCode{" +
                "formatted_address='" + formatted_address + '\'' +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", citycode='" + citycode + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", township='" + township + '\'' +
                ", neighborhood=" + neighborhood +
                ", building=" + building +
                ", adcode='" + adcode + '\'' +
                ", street='" + street + '\'' +
                ", number='" + number + '\'' +
                ", location='" + location + '\'' +
                ", level='" + level + '\'' +
                '}';
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTownship() {
        return township;
    }

    public void setTownship(String township) {
        this.township = township;
    }

    public List<AddInfo> getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(List<AddInfo> neighborhood) {
        this.neighborhood = neighborhood;
    }

    public List<AddInfo> getBuilding() {
        return building;
    }

    public void setBuilding(List<AddInfo> building) {
        this.building = building;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
