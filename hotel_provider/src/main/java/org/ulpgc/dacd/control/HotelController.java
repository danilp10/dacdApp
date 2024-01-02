package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Hotel;
import org.ulpgc.dacd.model.HotelBasicInfo;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.TimerTask;

public class HotelController extends TimerTask {
    private final OpenHotelMapSupplier hotelSupplier;
    private final JmsHotelStore jmsHotelStore;
    private final List<HotelBasicInfo> hotels;

    public HotelController(OpenHotelMapSupplier hotelSupplier, JmsHotelStore jmsHotelStore, List<HotelBasicInfo> hotels) {
        this.hotelSupplier = hotelSupplier;
        this.jmsHotelStore = jmsHotelStore;
        this.hotels = hotels;
    }

    @Override
    public void run() {
        Instant currentDay = Instant.now().truncatedTo(ChronoUnit.DAYS);
        int currentHour = Instant.now().atZone(ZoneId.of("UTC")).getHour();
        if (currentHour >= 12) {
            currentDay = currentDay.plus(1, ChronoUnit.DAYS);
            System.out.println(currentDay);
        }

        for (int i = 0; i < 5; i++) {
            Instant checkIn = currentDay.plus(i, ChronoUnit.DAYS);

            for (int j = i; j < 5; j++) {
                Instant checkout = checkIn.plus(j - i, ChronoUnit.DAYS);

                try {
                    String filepath = "hotel_provider/src/main/resources/hotel.json";
                    List<Hotel> hotelList = hotelSupplier.getHotel(filepath, checkIn, checkout);

                    for (Hotel hotel : hotelList) {
                        jmsHotelStore.save(hotel);
                    }
                    hotelList.clear();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


