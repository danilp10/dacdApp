package org.ulpgc.dacd.model;

import java.time.Instant;

public class Hotel {
    private  Instant ts;
    private String name;
    private String location;
    private String key;
    private String code;
    private String rateName;
    private double rate;
    private double tax;
    private Instant checkIn;
    private Instant checkOut;

    public Hotel(Instant ts, String name, String location, String key, String code, String rateName, double rate, double tax, Instant checkIn, Instant checkOut) {
        this.ts = ts;
        this.name = name;
        this.location = location;
        this.key = key;
        this.code = code;
        this.rateName = rateName;
        this.rate = rate;
        this.tax = tax;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public Instant getTs() {
        return ts;
    }
    public String getName() {
        return name;
    }
    public String getLocation() {
        return location;
    }

    public String getKey() {
        return key;
    }

    public String getCode() {
        return code;
    }

    public String getRateName() {
        return rateName;
    }

    public double getRate() {
        return rate;
    }

    public double getTax() {
        return tax;
    }

    public Instant getCheckIn() {
        return checkIn;
    }

    public Instant getCheckOut() {
        return checkOut;
    }
}
