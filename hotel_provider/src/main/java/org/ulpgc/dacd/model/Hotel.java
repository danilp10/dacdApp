package org.ulpgc.dacd.model;

import java.time.Instant;
import java.util.Objects;

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
    private String ss;

    public Hotel(Instant ts, String name, String location, String key, String code, String rateName, double rate, double tax, Instant checkIn, Instant checkOut, String ss) {
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
        this.ss = ss;
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

    public String getSs() {
        return ss;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hotel hotel = (Hotel) o;
        return Objects.equals(key, hotel.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

}
