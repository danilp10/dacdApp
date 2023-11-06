package org.ulpgc.dacd.control;

import org.ulpgc.dacd.model.Weather;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
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
                        + "temperature REAL PRIMARY KEY, "
                        + "precipitation REAL, "
                        + "humidity REAL, "
                        + "windSpeed REAL)")) {
            // Crea la tabla correspondiente a la isla si no existe
            statement.executeUpdate();
        } catch (SQLException e) {
            // Maneja las excepciones al ejecutar las sentencias SQL
            e.printStackTrace();
        }
    }

    @Override
    public void insertWeatherData(String islandName, Weather data) {
        Instant instant = data.getTs();

        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO " + islandName + " (temperature, precipitation, humidity, windSpeed) "
                        + "VALUES (?, ?, ?, ?)")) {
            // Inserta los registros con los datos meteorológicos en la tabla correspondiente
            // long epochMilli = data.getTs().toEpochMilli();
            // statement.setString(1, String.valueOf(epochMilli));
            // statement.setString(2, String.valueOf(data.getLocation()));
            statement.setDouble(1, data.getTemp());
            statement.setDouble(2, data.getRain());
            statement.setDouble(3, data.getHumidity());
            statement.setDouble(4, data.getWind());
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
