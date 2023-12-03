package org.ulpgc.dacd.control;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.ulpgc.dacd.model.Location;
import org.ulpgc.dacd.model.Weather;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

public class JmsWeatherStore {

    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String subject = "prediction.Weather";

    public void sendWeatherListToQueue(List<Weather> weatherList) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(subject);
        MessageProducer producer = session.createProducer(destination);

        for (Weather weather : weatherList) {
            String jsonWeather = weather.toJson();
            // Imprimir el evento serializado antes de enviarlo
            System.out.println("Serialized Weather Event: " + jsonWeather);
            TextMessage message = session.createTextMessage(jsonWeather);
            producer.send(message);
        }

        System.out.println("Weather data sent to the queue.");
        connection.close();
    }



    public static void main(String[] args) {
        try {
            JmsWeatherStore jmsWeatherStore = new JmsWeatherStore();

            OpenWeatherMapSupplier weatherSupplier = new OpenWeatherMapSupplier(args[0]);

            Location[] locations = Main.getLocations();

            for (Location location : locations) {
                Instant instant = Instant.now();
                List<Weather> weatherList = weatherSupplier.getWeather(location, instant);
                jmsWeatherStore.sendWeatherListToQueue(weatherList);
            }
        } catch (JMSException | IOException e) {
            e.printStackTrace();
        }
    }

}
