package org.ulpgc.dacd.model;

import com.google.gson.Gson;

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

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
