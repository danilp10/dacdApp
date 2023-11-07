package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Weather;

import java.sql.*;
import java.time.Instant;

public class SqliteWeatherStore implements WeatherStore{
    private Connection connection;

    public void connect(String databaseURL) throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(databaseURL);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTableForIsland(String islandName) {
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS " + islandName + " ("
                        + "instant TEXT PRIMARY KEY, "
                        + "temperature REAL, "
                        + "precipitation REAL, "
                        + "humidity REAL, "
                        + "windSpeed REAL, "
                        + "clouds INT)")) {
            // Crea la tabla correspondiente a la isla si no existe
            statement.executeUpdate();
        } catch (SQLException e) {
            // Maneja las excepciones al ejecutar las sentencias SQL
            e.printStackTrace();
        }
    }

    @Override
    public void insertWeatherData(String islandName, Weather data) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO " + islandName + " (instant, temperature, precipitation, humidity, windSpeed, clouds) "
                        + "VALUES (?, ?, ?, ?, ?, ?)")) {
            // Inserta los registros con los datos meteorológicos en la tabla correspondiente
            // long epochMilli = data.getTs().toEpochMilli();
            // statement.setString(1, String.valueOf(epochMilli));
            // statement.setString(2, String.valueOf(data.getLocation()));
            statement.setString(1, String.valueOf(data.getTs()));
            statement.setDouble(2, data.getTemp());
            statement.setDouble(3, data.getRain());
            statement.setDouble(4, data.getHumidity());
            statement.setDouble(5, data.getWind());
            statement.setInt(6, data.getClouds());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void delete(Statement statement) throws SQLException {
        statement.execute("DELETE FROM products\n" +
                "WHERE id=1;");
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
