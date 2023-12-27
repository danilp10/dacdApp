package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Hotel;
import java.io.IOException;
import java.util.List;

public interface HotelSupplier {
    List<Hotel> getHotel(String filename, String chk_in, String chk_out) throws IOException;
}
