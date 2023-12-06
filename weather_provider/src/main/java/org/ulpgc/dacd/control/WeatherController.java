package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.TimerTask;

public class WeatherController extends TimerTask{
    private OpenWeatherMapSupplier weatherSupplier;
    private JmsWeatherStore jmsWeatherStore;

    public WeatherController(OpenWeatherMapSupplier weatherSupplier, JmsWeatherStore jmsWeatherStore) {
        this.weatherSupplier = weatherSupplier;
        this.jmsWeatherStore = jmsWeatherStore;
    }

    @Override
    public void run() {
        Location[] locations = Main.getLocations();
        Instant instant = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        for (Location location : locations) {
            List<Weather> weatherList;
            try {
                weatherList = weatherSupplier.getWeather(location, instant);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            jmsWeatherStore.save(weatherList);
        }
    }

}
