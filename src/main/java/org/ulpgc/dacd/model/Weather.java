package org.ulpgc.dacd.model;

import java.time.Instant;

public class Weather {
    private Instant ts;
    private Location location;
    private double rain;
    private double wind;
    private double temp;
    private double humidity;

    public Weather(Instant ts, Location location, double rain, double wind, double temp, double humidity) {
        if (ts == null) {
            this.ts = Instant.now();
        } else {
            this.ts = ts;
        }
        this.location = location;
        this.rain = rain;
        this.wind = wind;
        this.temp = temp;
        this.humidity = humidity;
    }

    public Instant getTs() {
        return ts;
    }

    public void setTs(Instant ts) {
        this.ts = ts;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getRain() {
        return rain;
    }

    public void setRain(double rain) {
        this.rain = rain;
    }

    public double getWind() {
        return wind;
    }

    public void setWind(double wind) {
        this.wind = wind;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }
}
