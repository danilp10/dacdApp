package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;

import javax.jms.JMSException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Timer;

public class Main {
    public static void main(String[] args) throws SQLException {
        String apikey = args[0];
        OpenWeatherMapSupplier openWeatherMapSupplier = new OpenWeatherMapSupplier(apikey);
        WeatherController weatherController = new WeatherController(openWeatherMapSupplier);

        Timer timer = new Timer();
        long period = 6 * 60 * 60 * 1000;
        timer.scheduleAtFixedRate(weatherController, 0, period);

        try {
            JmsWeatherStore jmsWeatherStore = new JmsWeatherStore();

            OpenWeatherMapSupplier weatherSupplier = new OpenWeatherMapSupplier(args[0]);

            Location[] locations = Main.getLocations();

            for (Location location : locations) {
                Instant instant = Instant.now().truncatedTo(ChronoUnit.SECONDS);
                List<Weather> weatherList = weatherSupplier.getWeather(location, instant);
                jmsWeatherStore.sendWeatherListToQueue(weatherList);
            }
        } catch (JMSException | IOException e) {
            e.printStackTrace();
        }
    }

    public static Location[] getLocations() {
        return new Location[] {
                new Location(28.29, -16.62, "Tenerife"),
                new Location(27.99, -15.60, "GranCanaria"),
                new Location(29.04, -13.58, "Lanzarote"),
                new Location(28.35, -14.05, "Fuerteventura"),
                new Location(28.61, -17.87, "LaPalma"),
                new Location(28.11, -17.22, "LaGomera"),
                new Location(27.74, -18.01, "ElHierro"),
                new Location(29.23, -13.51, "LaGraciosa")
        };
    }
}