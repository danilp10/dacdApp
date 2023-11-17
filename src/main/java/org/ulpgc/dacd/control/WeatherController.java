package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.TimerTask;

public class WeatherController extends TimerTask{
    private OpenWeatherMapSupplier openWeatherMapSupplier;
    private  SqliteWeatherStore sqliteWeatherStore;

    public WeatherController(OpenWeatherMapSupplier openWeatherMapSupplier, SqliteWeatherStore sqliteWeatherStore) {
        this.openWeatherMapSupplier = openWeatherMapSupplier;
        this.sqliteWeatherStore = sqliteWeatherStore;
    }

    @Override
    public void run() {
        Location[] locations = Main.getLocations();
        for (Location location : locations) {
            for (int i = 0; i < 5; i++) {
                Instant scheduledInstant = Instant.now().truncatedTo(ChronoUnit.DAYS)
                        .plus(i, ChronoUnit.DAYS).plus(12, ChronoUnit.HOURS);

                try {
                    List<Weather> weather = openWeatherMapSupplier.getWeather(location, scheduledInstant);
                    sqliteWeatherStore.createTableForIsland(location.getIsland());
                    for (Weather data : weather) {
                        sqliteWeatherStore.insertWeather(location.getIsland(), data);
                    }
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
