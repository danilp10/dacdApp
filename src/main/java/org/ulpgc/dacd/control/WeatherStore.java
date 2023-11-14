package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Weather;


public interface WeatherStore {
    void createTableForIsland(String islandName);
    void insertWeather(String islandName, Weather weather);
    void deleteTableForIsland(String islandName);
    void closeConnection();
}
