package org.ulpgc.dacd.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.ulpgc.dacd.model.Hotel;
import org.ulpgc.dacd.model.Weather;

import javax.jms.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventConsumer {
    private static final Logger LOGGER = Logger.getLogger(EventConsumer.class.getName());

    private static final String BROKER_URL = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static final String WEATHER_TOPIC = "prediction.Weather";
    private static final String HOTEL_TOPIC = "rate.Hotel";
    private static final String DATABASE_URL = "jdbc:sqlite:datamart.db";
    private final DatamartConnection datamartConnection;

    public EventConsumer() {
        datamartConnection = new DatamartConnection();
        try {
            datamartConnection.connect(DATABASE_URL);
            datamartConnection.createTable();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error connecting to the database or creating table", e);
            throw new RuntimeException("Failed to initialize EventConsumer", e);
        }
    }

    public void consumeEvents() {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic weatherTopic = session.createTopic(WEATHER_TOPIC);
            Topic hotelTopic = session.createTopic(HOTEL_TOPIC);

            MessageConsumer weatherConsumer = session.createConsumer(weatherTopic);
            MessageConsumer hotelConsumer = session.createConsumer(hotelTopic);

            weatherConsumer.setMessageListener(this::processWeatherMessage);
            hotelConsumer.setMessageListener(this::processHotelMessage);

        } catch (JMSException e) {
            LOGGER.log(Level.SEVERE, "JMS Exception while consuming events", e);
            throw new RuntimeException("Failed to consume events", e);
        }
    }

    private void processWeatherMessage(Message message) {
        processMessage(message, "Weather");
    }

    private void processHotelMessage(Message message) {
        processMessage(message, "Hotel");
    }

    private void processMessage(Message message, String messageType) {
        try {
            String event = ((TextMessage) message).getText();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            if ("Weather".equals(messageType)) {
                Weather weather = objectMapper.readValue(event, Weather.class);
                datamartConnection.saveWeather(weather);
            } else if ("Hotel".equals(messageType)) {
                Hotel hotel = objectMapper.readValue(event, Hotel.class);
                datamartConnection.saveHotel(hotel);
            }

        } catch (JMSException | SQLException | IOException e) {
            LOGGER.log(Level.SEVERE, "Error processing message", e);
        }
    }
}
