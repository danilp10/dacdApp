package org.ulpgc.dacd.model;

public class HotelBasicInfo {
    private String hotelName;
    private String key;
    private String location;

    public HotelBasicInfo(String hotelName, String key, String location) {
        this.hotelName = hotelName;
        this.key = key;
        this.location = location;
    }

    public String getHotelName() {
        return hotelName;
    }

    public String getHotelKey() {
        return key;
    }

    public String getLocation() {
        return location;
    }

}
