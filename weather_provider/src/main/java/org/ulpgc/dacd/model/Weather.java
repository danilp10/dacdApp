package org.ulpgc.dacd.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;

public class Weather implements Serializable {
    private Instant ts;
    private double rain;
    private double windSpeed;
    private double temp;
    private int humidity;
    private int clouds;
    private double lat;
    private double lon;
    private String island;
    private String ss;
    private Instant predictionTime;
    private Location location;

    public Weather(Instant ts, double rain, double windSpeed, double temp, int humidity, int clouds, Location location, double lat, double lon, String island, String ss, Instant predictionTime) {
        this.ts = ts;
        this.rain = rain;
        this.windSpeed = windSpeed;
        this.temp = temp;
        this.humidity = humidity;
        this.clouds = clouds;
        this.location = new Location(lat, lon, island);
        this.lon = lon;
        this.island = island;
        this.ss = ss;
        this.predictionTime = predictionTime;
    }

    public Instant getTs() {
            return ts;
        }

    public double getRain() {
            return rain;
        }

    public double getWindSpeed() {
            return windSpeed;
        }

    public double getTemp() {
            return temp;
        }

    public int getHumidity() {
            return humidity;
        }

    public int getClouds() {
            return clouds;
        }

    public double getLat() {
            return lat;
        }

    public double getLon() {
            return lon;
        }

    public String getIsland() {
            return island;
        }

    public String getSs() {
        return ss;
    }

    public Location getLocation() {
        return location;
    }
}
