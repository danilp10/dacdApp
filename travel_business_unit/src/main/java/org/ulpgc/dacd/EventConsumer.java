package org.ulpgc.dacd;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.ulpgc.dacd.model.Hotel;
import org.ulpgc.dacd.model.Weather;

import javax.jms.*;
import java.io.IOException;
import java.sql.SQLException;

public class EventConsumer {
    private static final String BROKER_URL = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static final String WEATHER_TOPIC = "prediction.Weather";
    private static final String HOTEL_TOPIC = "rate.Hotel";
    private static final String DATABASE_URL = "jdbc:sqlite:datamart.db";

    private DatamartConnection datamartConnection;

    public EventConsumer() {
        datamartConnection = new DatamartConnection();
        try {
            datamartConnection.connect(DATABASE_URL);
            datamartConnection.createTable();
        } catch (SQLException e) {
            e.printStackTrace();
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

            weatherConsumer.setMessageListener(message -> {
                try {
                    String event = ((TextMessage) message).getText();
                    ObjectMapper objectMapper = new ObjectMapper();
                    Weather weather = objectMapper.readValue(event, Weather.class);
                    ObjectMapper objectMapper2 = new ObjectMapper();
                    Hotel hotel = objectMapper2.readValue(event, Hotel.class);
                    datamartConnection.save(weather, hotel);
                } catch (JMSException | SQLException | IOException e) {
                    e.printStackTrace();
                }
            });

            hotelConsumer.setMessageListener(message -> {
                try {
                    String event = ((TextMessage) message).getText();
                    // Puedes manejar los eventos del hotel aquí si es necesario.
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });

            while (true) {
                Thread.sleep(1000);
            }

        } catch (JMSException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
