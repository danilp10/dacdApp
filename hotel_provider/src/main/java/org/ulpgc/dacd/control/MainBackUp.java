package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.HotelBasicInfo;

import java.io.IOException;
import java.util.List;
import java.util.Timer;

public class MainBackUp {
    public static void main(String[] args) throws IOException {
        OpenHotelMapSupplier openHotelMapSupplier = new OpenHotelMapSupplier();
        JmsHotelStore jmsHotelStore = new JmsHotelStore();
        String filePath = "hotel_provider/src/main/resources/hotel.json";
        List<HotelBasicInfo> hotels = new HotelFileReader(filePath).readHotels();
        HotelController hotelController = new HotelController(openHotelMapSupplier, jmsHotelStore, hotels);

        Timer timer = new Timer();
        long period = 6 * 60 * 60 * 1000;
        timer.scheduleAtFixedRate(hotelController, 0, period);
    }
}