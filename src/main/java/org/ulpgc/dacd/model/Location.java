package org.ulpgc.dacd.model;

public class Location {
    private double lat;
    private double lon;
    private String island;

    public Location(double lat, double lon, String island) {
        this.lat = lat;
        this.lon = lon;
        this.island = island;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getIsland() {
        return island;
    }

    public void setIsland(String island) {
        this.island = island;
    }
}
