package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;

import java.io.IOException;
import java.time.Instant;

public interface WeatherSupplier {

    static void getWeather(Location location, Instant instant) throws IOException {

    }
}
