package org.ulpgc.dacd.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TravelAppGUI {

    private JFrame frame;
    private JComboBox<String> destinationComboBox;
    private JTextArea weatherHotelInfoArea;

    public TravelAppGUI() {
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("Travel App");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        String[] destinations = {"Barcelona", "Paris", "Madrid", "Berlin", "Rome", "Brussels", "London", "Stockholm"};
        destinationComboBox = new JComboBox<>(destinations);
        frame.add(destinationComboBox);

        JButton showInfoButton = new JButton("Mostrar Info");
        showInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayInfoForSelectedDestination();
            }
        });
        frame.add(showInfoButton);

        weatherHotelInfoArea = new JTextArea(10, 30);
        frame.add(new JScrollPane(weatherHotelInfoArea));

        frame.setVisible(true);
    }

    private void displayInfoForSelectedDestination() {
        String selectedDestination = (String) destinationComboBox.getSelectedItem();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:datamart.db")) {

            String weatherQuery = "SELECT predictionTime, rain, windSpeed, temp, humidity, clouds, lat, lon FROM weather WHERE city = ?";
            try (PreparedStatement stmt = conn.prepareStatement(weatherQuery)) {
                stmt.setString(1, selectedDestination);
                ResultSet rs = stmt.executeQuery();

                StringBuilder weatherInfo = new StringBuilder("Predicciones del clima para " + selectedDestination + ":\n");
                while (rs.next()) {
                    weatherInfo.append("Fecha: ").append(rs.getString("predictionTime"))
                            .append(", Lluvia: ").append(rs.getDouble("rain"))
                            .append(", Velocidad del viento: ").append(rs.getDouble("windSpeed"))
                            .append(", Temperatura: ").append(rs.getDouble("temp"))
                            .append(", Humedad: ").append(rs.getInt("humidity"))
                            .append(", Nubosidad: ").append(rs.getInt("clouds"))
                            .append(", Latitud: ").append(rs.getDouble("lat"))
                            .append(", Longitud: ").append(rs.getDouble("lon"))
                            .append("\n");
                }

                String hotelQuery = "SELECT key, name, location, code, rateName, rate, tax, checkIn, checkOut FROM hotels WHERE location = ?";
                try (PreparedStatement hotelStmt = conn.prepareStatement(hotelQuery)) {
                    hotelStmt.setString(1, selectedDestination);
                    ResultSet hotelRs = hotelStmt.executeQuery();

                    StringBuilder hotelInfo = new StringBuilder("Disponibilidad de los hoteles de la zona:\n");
                    while (hotelRs.next()) {
                        String checkInStr = hotelRs.getString("checkIn");
                        String checkOutStr = hotelRs.getString("checkOut");

                        Instant checkIn = Instant.parse(checkInStr);
                        Instant checkOut = Instant.parse(checkOutStr);
                        long nights = ChronoUnit.DAYS.between(checkIn.truncatedTo(ChronoUnit.DAYS), checkOut.truncatedTo(ChronoUnit.DAYS));
                        double totalPrice = hotelRs.getDouble("rate") * nights + hotelRs.getDouble("tax");

                        hotelInfo.append("- ").append(hotelRs.getString("name"))
                                .append(", Código: ").append(hotelRs.getString("code"))
                                .append(", Tarifa por noche: ").append(hotelRs.getDouble("rate"))
                                .append(", Impuesto: ").append(hotelRs.getDouble("tax"))
                                .append(", Total para ").append(nights).append(" noches: ").append(totalPrice)
                                .append(", Check-In: ").append(checkInStr)
                                .append(", Check-Out: ").append(checkOutStr)
                                .append("\n");
                    }

                    weatherHotelInfoArea.setText(weatherInfo.toString() + "\n" + hotelInfo.toString());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

