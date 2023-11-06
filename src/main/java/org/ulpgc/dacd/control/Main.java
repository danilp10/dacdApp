package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Weather;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Timer;

public class Main {
    public static void main(String[] args) throws SQLException {
        String apikey = "3f7e30bbced203f4b907d03ba08d8ac6";

        OpenWeatherMapSupplier openWeatherMapSupplier = new OpenWeatherMapSupplier();
        SqliteWeatherStore sqliteWeatherStore = new SqliteWeatherStore();
        sqliteWeatherStore.connect("jdbc:sqlite:weather.db");
        sqliteWeatherStore.createTableForIsland("Lanzarote");
        sqliteWeatherStore.createTableForIsland("La_Gomera");

        Timer timer = new Timer();
        long period = 6 * 60 * 60 * 1000; // 6 * 60 * 60 * 1000
        timer.scheduleAtFixedRate(new WeatherController(openWeatherMapSupplier, sqliteWeatherStore, "La_Gomera"), 0, period);
    }
}