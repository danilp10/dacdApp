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
    private static String subject = "prediction.Weather"; // Change the queue name to match the one used in WeatherController

    public void sendWeatherListToQueue(List<Weather> weatherList) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(subject);
        MessageProducer producer = session.createProducer(destination);

        for (Weather weather : weatherList) {
            String jsonWeather = weather.toJson();
            TextMessage message = session.createTextMessage(jsonWeather);
            producer.send(message);
        }

        System.out.println("Weather data sent to the queue.");
        connection.close();
    }


    public static void main(String[] args) {
        try {
            JmsWeatherStore jmsWeatherStore = new JmsWeatherStore();

            // Crear una instancia de OpenWeatherMapSupplier con tu clave de API
            OpenWeatherMapSupplier weatherSupplier = new OpenWeatherMapSupplier(args[0]);

            // Obtener la lista de Weather usando el método getWeather
            Location location = new Location(27.99, -15.60, "GranCanaria");
            Instant instant = Instant.now(); // Puedes establecer el instante deseado
            List<Weather> weatherList = weatherSupplier.getWeather(location, instant);

            // Enviar la lista de Weather a la cola JMS
            jmsWeatherStore.sendWeatherListToQueue(weatherList);
        } catch (JMSException | IOException e) {
            e.printStackTrace();
        }
    }

}
