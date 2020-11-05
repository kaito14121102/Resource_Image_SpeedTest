package com.gdt.speedtest.data.model;

public class Address {
    private String url;
    private String lat;
    private String lon;
    private String distance;
    private String name;
    private String country;
    private String cc;
    private String sponsor;
    private String id;
    private String preferred;
    private String https_functional;
    private String host;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPreferred() {
        return preferred;
    }

    public void setPreferred(String preferred) {
        this.preferred = preferred;
    }

    public String getHttps_functional() {
        return https_functional;
    }

    public void setHttps_functional(String https_functional) {
        this.https_functional = https_functional;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "Address{" +
                "url='" + url + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", distance='" + distance + '\'' +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", cc='" + cc + '\'' +
                ", sponsor='" + sponsor + '\'' +
                ", id='" + id + '\'' +
                ", preferred='" + preferred + '\'' +
                ", https_functional='" + https_functional + '\'' +
                ", host='" + host + '\'' +
                '}';
    }

    public Address(String url, String lat, String lon, String distance, String name, String country, String cc, String sponsor, String id, String preferred, String https_functional, String host) {

        this.url = url;
        this.lat = lat;
        this.lon = lon;
        this.distance = distance;
        this.name = name;
        this.country = country;
        this.cc = cc;
        this.sponsor = sponsor;
        this.id = id;
        this.preferred = preferred;
        this.https_functional = https_functional;
        this.host = host;

    }
}

