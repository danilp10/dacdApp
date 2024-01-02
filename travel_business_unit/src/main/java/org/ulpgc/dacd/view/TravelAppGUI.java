package org.ulpgc.dacd.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

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

        // Combo box de destinos
        String[] destinations = {"Barcelona", "Paris", "Madrid", "Berlin", "Rome", "Brussels", "London", "Stockholm"};
        destinationComboBox = new JComboBox<>(destinations);
        frame.add(destinationComboBox);

        // Botón para mostrar información
        JButton showInfoButton = new JButton("Mostrar Info");
        showInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayInfoForSelectedDestination();
            }
        });
        frame.add(showInfoButton);

        // Área de texto para mostrar información
        weatherHotelInfoArea = new JTextArea(10, 30);
        frame.add(new JScrollPane(weatherHotelInfoArea));

        frame.setVisible(true);
    }

    private void displayInfoForSelectedDestination() {
        String selectedDestination = (String) destinationComboBox.getSelectedItem();

        // Inicializar la conexión
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:datamart.db")) {

            // Consulta para obtener el clima
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

                // Consulta para obtener hoteles
                String hotelQuery = "SELECT key, name, location, code, rateName, rate, tax, checkIn, checkOut FROM hotels WHERE location = ?";
                try (PreparedStatement hotelStmt = conn.prepareStatement(hotelQuery)) {
                    hotelStmt.setString(1, selectedDestination);
                    ResultSet hotelRs = hotelStmt.executeQuery();

                    StringBuilder hotelInfo = new StringBuilder("Disponibilidad de los hoteles de la zona:\n");
                    while (hotelRs.next()) {
                        hotelInfo.append("- ").append(hotelRs.getString("name"))
                                .append(", Código: ").append(hotelRs.getString("code"))
                                .append(", Tarifa: ").append(hotelRs.getDouble("rate"))
                                .append(", Impuesto: ").append(hotelRs.getDouble("tax"))
                                .append(", Check-In: ").append(hotelRs.getString("checkIn"))
                                .append(", Check-Out: ").append(hotelRs.getString("checkOut"))
                                .append("\n");
                    }

                    weatherHotelInfoArea.setText(weatherInfo.toString() + "\n" + hotelInfo.toString());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TravelAppGUI();
            }
        });
    }
}

