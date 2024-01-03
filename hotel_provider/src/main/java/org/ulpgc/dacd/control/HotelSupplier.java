package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Hotel;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

public interface HotelSupplier {
    List<Hotel> getHotel(String filename, Instant chk_in, Instant chk_out) throws IOException;
}
