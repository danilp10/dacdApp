package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Weather;

import java.sql.SQLException;
import java.util.List;


public interface WeatherStore {
    void save(List<Weather> weatherList);
}
