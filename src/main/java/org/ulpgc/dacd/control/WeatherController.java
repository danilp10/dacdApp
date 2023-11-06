package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Weather;

import java.io.IOException;
import java.util.TimerTask;

public class WeatherController extends TimerTask{
    private OpenWeatherMapSupplier openWeatherMapSupplier;
    private  SqliteWeatherStore sqliteWeatherStore;
    private String islandName;

    public WeatherController(OpenWeatherMapSupplier openWeatherMapSupplier, SqliteWeatherStore sqliteWeatherStore, String islandName) {
        this.openWeatherMapSupplier = openWeatherMapSupplier;
        this.sqliteWeatherStore = sqliteWeatherStore;
        this.islandName = islandName;
    }

    @Override
    public void run() {
        try {
            double lat = 28.291563;
            double lon = -16.629130;
            Weather weatherData = OpenWeatherMapSupplier.getWeather(lat, lon);

            // A continuación, puedes almacenar los datos en la base de datos SQLite:
            sqliteWeatherStore.createTableForIsland(islandName);
            sqliteWeatherStore.insertWeatherData(islandName, weatherData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public OpenWeatherMapSupplier getOpenWeatherMapSupplier() {
        return openWeatherMapSupplier;
    }

    public void setOpenWeatherMapSupplier(OpenWeatherMapSupplier openWeatherMapSupplier) {
        this.openWeatherMapSupplier = openWeatherMapSupplier;
    }

    public SqliteWeatherStore getSqliteWeatherStore() {
        return sqliteWeatherStore;
    }

    public void setSqliteWeatherStore(SqliteWeatherStore sqliteWeatherStore) {
        this.sqliteWeatherStore = sqliteWeatherStore;
    }

    public String getIslandName() {
        return islandName;
    }

    public void setIslandName(String islandName) {
        this.islandName = islandName;
    }
}
