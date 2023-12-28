package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Hotel;
import org.ulpgc.dacd.model.HotelBasicInfo;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class HotelController extends TimerTask {
    private final OpenHotelMapSupplier hotelSupplier;
    private final JmsHotelStore jmsHotelStore;
    private final List<HotelBasicInfo> hotels;
    private final int eventsLimit = 40;
    private int eventsCounter = 0;

    public HotelController(OpenHotelMapSupplier hotelSupplier, JmsHotelStore jmsHotelStore, List<HotelBasicInfo> hotels) {
        this.hotelSupplier = hotelSupplier;
        this.jmsHotelStore = jmsHotelStore;
        this.hotels = hotels;
    }

    @Override
    public void run() {
        List<Hotel> uniqueHotelList = new ArrayList<>();

        while (eventsCounter <= eventsLimit) {
            try {
                Instant checkIn;
                int currentHour = Instant.now().atZone(ZoneId.of("UTC")).getHour();
                if (currentHour >= 17) {
                    checkIn = Instant.now().plus(12, ChronoUnit.HOURS).truncatedTo(ChronoUnit.DAYS);
                } else {
                    checkIn = Instant.now().truncatedTo(ChronoUnit.DAYS);
                }
                Instant checkOut = checkIn.plus(5, ChronoUnit.DAYS);
                String filepath = "hotel_provider/src/main/resources/hotel.json";
                List<Hotel> hotelList = hotelSupplier.getHotel(filepath, checkIn, checkOut);

                for (Hotel hotel : hotelList) {
                    if (!uniqueHotelList.contains(hotel)) {
                        uniqueHotelList.add(hotel);
                        eventsCounter++;
                    }
                    if (eventsCounter > eventsLimit) {
                        break;
                    }
                }

                for (Hotel hotel : uniqueHotelList) {
                    jmsHotelStore.save(hotel);
                }
                uniqueHotelList.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        eventsCounter = 0;
    }

}


