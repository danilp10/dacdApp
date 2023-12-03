package org.ulpgc.dacd.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

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

    public static Gson prepareGson() {
        return new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Instant.class, new TypeAdapter<Instant>() {
            @Override
            public void write(JsonWriter out, Instant value) throws IOException {
                out.value(value.toString());
            }

            @Override
            public Instant read(JsonReader in) throws IOException {
                return Instant.parse(in.nextString());
            }
        }).create();
    }
}
