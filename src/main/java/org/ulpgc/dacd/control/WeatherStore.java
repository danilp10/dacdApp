package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Weather;


public interface WeatherStore {
    void createTableForIsland(String islandName);
    void insertWeatherData(String islandName, Weather weatherData);

}
