package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Weather;
import org.ulpgc.dacd.control.WeatherStore;

import java.sql.*;
import java.time.Instant;

public class SqliteWeatherStore implements WeatherStore{
    private Connection connection;

    @Override
    public void save(Weather weather) throws SQLException {
        String islandName = weather.getLocation().getIsland();
        boolean recordExists = recordExists(islandName, weather.getTs());
        try {
            if (recordExists) {
                insertWeather(islandName, weather);
            } else {
                insertWeather(islandName, weather);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void connect(String databaseURL) throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(databaseURL);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void createTableForIsland(String islandName) {
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS " + islandName + " ("
                        + "date TEXT PRIMARY KEY, "
                        + "temperature REAL, "
                        + "precipitation REAL, "
                        + "humidity INT, "
                        + "windSpeed REAL, "
                        + "clouds INT)")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertWeather(String islandName, Weather data) throws SQLException {
        boolean recordExists = recordExists(islandName, data.getTs());
        try {
            if (recordExists) {
                try (PreparedStatement statement = connection.prepareStatement(
                        "UPDATE " + islandName + " SET temperature=?, precipitation=?, humidity=?, windSpeed=?, clouds=? WHERE date=?")) {
                    statement.setDouble(1, data.getTemp());
                    statement.setDouble(2, data.getRain());
                    statement.setInt(3, data.getHumidity());
                    statement.setDouble(4, data.getWindSpeed());
                    statement.setInt(5, data.getClouds());
                    statement.setString(6, String.valueOf(data.getTs()));
                    statement.executeUpdate();
                }
            } else {
                try (PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO " + islandName + " (date, temperature, precipitation, humidity, windSpeed, clouds) "
                                + "VALUES (?, ?, ?, ?, ?, ?)")) {
                    statement.setString(1, String.valueOf(data.getTs()));
                    statement.setDouble(2, data.getTemp());
                    statement.setDouble(3, data.getRain());
                    statement.setInt(4, data.getHumidity());
                    statement.setDouble(5, data.getWindSpeed());
                    statement.setInt(6, data.getClouds());
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean recordExists(String islandName, Instant ts) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM " + islandName + " WHERE date=?")) {
            statement.setString(1, String.valueOf(ts));
            ResultSet result = statement.executeQuery();
            return result.next() && result.getInt(1) > 0;
        }
    }

    public void deleteTableForIsland(String islandName) {
        try (PreparedStatement statement = connection.prepareStatement("DROP TABLE IF EXISTS " + islandName)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
