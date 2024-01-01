package org.ulpgc.dacd;

import org.ulpgc.dacd.model.Hotel;
import org.ulpgc.dacd.model.Weather;

import java.sql.*;
import java.time.Instant;

public class DatamartConnection {
    private Connection connection;

    public void connect(String databaseURL) throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(databaseURL);
            System.out.println("Connection to SQLite has been established.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void save(Weather weather, Hotel hotel) throws SQLException {
        boolean recordExists = recordExists(weather.getLocation().getCity(), weather.getTs());

        try {
            if (recordExists) {
                updateRecord(weather, hotel);
            } else {
                insertRecord(weather, hotel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable() {
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS datamart ("
                        + "location TEXT PRIMARY KEY, "
                        + "date TEXT, "
                        + "temperature REAL, "
                        + "precipitation REAL, "
                        + "humidity INT, "
                        + "windSpeed REAL, "
                        + "clouds INT, "
                        + "hotelName TEXT, "
                        + "checkInOut TEXT, "
                        + "rate REAL, "
                        + "tax REAL, "
                        + "rateName TEXT, "
                        + "code TEXT)")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertRecord(Weather weather, Hotel hotel) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO datamart (location, date, temperature, precipitation, humidity, windSpeed, clouds, hotelName, checkInOut, rate, tax, rateName, code) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            statement.setString(1, weather.getLocation().getCity());
            statement.setString(2, String.valueOf(weather.getTs()));
            statement.setDouble(3, weather.getTemp());
            statement.setDouble(4, weather.getRain());
            statement.setInt(5, weather.getHumidity());
            statement.setDouble(6, weather.getWindSpeed());
            statement.setInt(7, weather.getClouds());
            statement.setString(8, hotel.getName());
            statement.setString(9, String.valueOf(hotel.getCheckIn()) + " to " + String.valueOf(hotel.getCheckOut()));
            statement.setDouble(10, hotel.getRate());
            statement.setDouble(11, hotel.getTax());
            statement.setString(12, hotel.getRateName());
            statement.setString(13, hotel.getCode());
            statement.executeUpdate();
        }
    }

    private void updateRecord(Weather weather, Hotel hotel) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE datamart SET temperature=?, precipitation=?, humidity=?, windSpeed=?, clouds=?, hotelName=?, checkInOut=?, rate=?, tax=?, rateName=?, code=? WHERE location=? AND date=?")) {
            statement.setString(1, weather.getLocation().getCity());
            statement.setString(2, String.valueOf(weather.getTs()));
            statement.setDouble(3, weather.getTemp());
            statement.setDouble(4, weather.getRain());
            statement.setInt(5, weather.getHumidity());
            statement.setDouble(6, weather.getWindSpeed());
            statement.setInt(7, weather.getClouds());
            statement.setString(8, hotel.getName());
            statement.setString(9, String.valueOf(hotel.getCheckIn()) + " to " + String.valueOf(hotel.getCheckOut()));
            statement.setDouble(10, hotel.getRate());
            statement.setDouble(11, hotel.getTax());
            statement.setString(12, hotel.getRateName());
            statement.setString(13, hotel.getCode());
            statement.executeUpdate();
        }
    }

    private boolean recordExists(String location, Instant ts) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM datamart WHERE location=? AND date=?")) {
            statement.setString(1, location);
            statement.setString(2, String.valueOf(ts));
            ResultSet result = statement.executeQuery();
            return result.next() && result.getInt(1) > 0;
        }
    }
}
