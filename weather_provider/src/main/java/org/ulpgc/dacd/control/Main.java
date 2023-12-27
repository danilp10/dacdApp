package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Location;
import java.util.Timer;

public class Main {
    public static void main(String[] args) {
        String apikey = args[0];
        OpenWeatherMapSupplier openWeatherMapSupplier = new OpenWeatherMapSupplier(apikey);
        JmsWeatherStore jmsWeatherStore = new JmsWeatherStore();
        WeatherController weatherController = new WeatherController(openWeatherMapSupplier, jmsWeatherStore);

        Timer timer = new Timer();
        long period = 6 * 60 * 60 * 1000;
        timer.scheduleAtFixedRate(weatherController, 0, period);
    }

    public static Location[] getLocations() {
        return new Location[] {
                new Location(41.39, 2.17, "Barcelona"),
                new Location(27.99, -15.60, "GranCanaria"),
                new Location(40.42, -3.70, "Madrid"),
                new Location(48.86, 2.35, "Paris"),
                new Location(52.52, 13.41, "Berlin"),
                new Location(51.51, -0.13, "London"),
                new Location(59.33, 18.07, "Stockholm"),
                new Location(50.85, 4.35, "Brussels")
        };
    }
}