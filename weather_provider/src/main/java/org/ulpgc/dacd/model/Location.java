package org.ulpgc.dacd.model;

public class Location {
    private double lat;
    private double lon;
    private String city;

    public Location(double lat, double lon, String city) {
        this.lat = lat;
        this.lon = lon;
        this.city = city;
    }

    public Location(){}

    public double getLat() {
        return lat;
    }
    public double getLon() {
        return lon;
    }
    public String getCity() {
        return city;
    }

}
