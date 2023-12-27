package org.ulpgc.dacd.model;

public class HotelBasicInfo {
    private String HotelName;
    private String key;
    private String Location;

    public HotelBasicInfo(String HotelName, String key, String Location) {
        this.HotelName = HotelName;
        this.key = key;
        this.Location = Location;
    }

    public String getHotelName() {
        return HotelName;
    }

    public String getHotelKey() {
        return key;
    }

    public String getLocation() {
        return Location;
    }

}
