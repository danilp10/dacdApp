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


    public void saveWeather(Weather weather) throws SQLException {
        boolean recordExists = weatherRecordExists(weather.getCity(), weather.getPredictionTime());

        try {
            if (recordExists) {
                updateWeatherRecord(weather);
            } else {
                insertWeatherRecord(weather);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveHotel(Hotel hotel) throws SQLException {
        boolean recordExists = hotelRecordExists(hotel.getCheckIn(), hotel.getCheckOut(), hotel.getKey());

        try {
            if (recordExists) {
                updateHotelRecord(hotel);
            } else {
                insertHotelRecord(hotel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable() {
        createHotelTable();
        createWeatherTable();
    }

    private void createHotelTable() {
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS hotels ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "key TEXT, "
                        + "name TEXT, "
                        + "location TEXT, "
                        + "code TEXT, "
                        + "rateName TEXT, "
                        + "rate REAL, "
                        + "tax REAL, "
                        + "checkIn TEXT, "
                        + "checkOut TEXT, "
                        + "UNIQUE(key, checkIn, checkOut))")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createWeatherTable() {
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS weather ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "city TEXT, "
                        + "predictionTime TEXT, "
                        + "rain REAL, "
                        + "windSpeed REAL, "
                        + "temp REAL, "
                        + "humidity INT, "
                        + "clouds INT, "
                        + "lat REAL, "
                        + "lon REAL, "
                        + "UNIQUE(city, predictionTime))")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertWeatherRecord(Weather weather) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO weather (predictionTime, rain, windSpeed, temp, humidity, clouds, lat, lon, city)"
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            statement.setString(1, String.valueOf(weather.getPredictionTime()));
            statement.setDouble(2, weather.getRain());
            statement.setDouble(3, weather.getWindSpeed());
            statement.setDouble(4, weather.getTemp());
            statement.setInt(5, weather.getHumidity());
            statement.setInt(6, weather.getClouds());
            statement.setDouble(7, weather.getLat());
            statement.setDouble(8, weather.getLon());
            statement.setString(9, weather.getCity());
            statement.executeUpdate();
        }
    }

    private void insertHotelRecord(Hotel hotel) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO hotels (key, name, location, code, rateName, rate, tax, checkIn, checkOut)"
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            statement.setString(1, hotel.getKey());
            statement.setString(2, hotel.getName());
            statement.setString(3, hotel.getLocation());
            statement.setString(4, hotel.getCode());
            statement.setString(5, hotel.getRateName());
            statement.setDouble(6, hotel.getRate());
            statement.setDouble(7, hotel.getTax());
            statement.setString(8, String.valueOf(hotel.getCheckIn()));
            statement.setString(9, String.valueOf(hotel.getCheckOut()));
            statement.executeUpdate();
        }
    }

    private void updateWeatherRecord(Weather weather) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE weather SET rain=?, windSpeed=?, temp=?, humidity=?, clouds=?, lat=?, lon=?, city=?, predictionTime=? WHERE city=? AND predictionTime=?")) {
            statement.setDouble(1, weather.getRain());
            statement.setDouble(2, weather.getWindSpeed());
            statement.setDouble(3, weather.getTemp());
            statement.setInt(4, weather.getHumidity());
            statement.setInt(5, weather.getClouds());
            statement.setDouble(6, weather.getLat());
            statement.setDouble(7, weather.getLon());
            statement.setString(8, weather.getCity());
            statement.setString(9, String.valueOf(weather.getPredictionTime()));
            statement.setString(10, weather.getCity());
            statement.setString(11, String.valueOf(weather.getPredictionTime()));
            statement.executeUpdate();
        }
    }


    private void updateHotelRecord(Hotel hotel) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE hotels SET name=?, location=?, code=?, rateName=?, rate=?, tax=?, checkIn=?, checkOut=? WHERE checkIn=? AND checkOut=? AND key=?")) {
            statement.setString(1, hotel.getName());
            statement.setString(2, hotel.getLocation());
            statement.setString(3, hotel.getCode());
            statement.setString(4, hotel.getRateName());
            statement.setDouble(5, hotel.getRate());
            statement.setDouble(6, hotel.getTax());
            statement.setString(7, String.valueOf(hotel.getCheckIn()));
            statement.setString(8, String.valueOf(hotel.getCheckOut()));
            statement.setString(9, String.valueOf(hotel.getCheckIn()));
            statement.setString(10, String.valueOf(hotel.getCheckOut()));
            statement.setString(11, hotel.getKey());
            statement.executeUpdate();
        }
    }


    private boolean hotelRecordExists(Instant checkIn, Instant checkOut, String key) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM hotels WHERE checkIn=? AND checkOut=? AND key=?")) {
            statement.setString(1, String.valueOf(checkIn));
            statement.setString(2, String.valueOf(checkOut));
            statement.setString(3, key);
            ResultSet result = statement.executeQuery();
            return result.next() && result.getInt(1) > 0;
        }
    }

    private boolean weatherRecordExists(String city, Instant predictionTime) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM weather WHERE city=? AND predictionTime=?")) {
            statement.setString(1, city);
            statement.setString(2, String.valueOf(predictionTime));
            ResultSet result = statement.executeQuery();
            return result.next() && result.getInt(1) > 0;
        }
    }

}