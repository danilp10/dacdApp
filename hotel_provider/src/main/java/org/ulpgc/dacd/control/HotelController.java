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
        for (HotelBasicInfo hotel : hotels) {
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
                jmsHotelStore.save(hotelList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
