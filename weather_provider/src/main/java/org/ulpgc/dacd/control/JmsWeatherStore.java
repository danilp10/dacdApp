package org.ulpgc.dacd.control;

import javax.jms.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.ulpgc.dacd.model.Weather;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

public class JmsWeatherStore implements WeatherStore{

    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String subject = "prediction.Weather";

    public void save(List<Weather> weatherList) {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(subject);
            MessageProducer producer = session.createProducer(topic);

            for (Weather weather : weatherList) {
                String jsonWeather = prepareGson().toJson(weather);
                System.out.println("Serialized Weather Event: " + jsonWeather);
                TextMessage message = session.createTextMessage(jsonWeather);
                producer.send(message);
            }

            System.out.println("Weather data sent to the queue.");
            connection.close();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }

    }

    public Gson prepareGson() {
        return new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Instant.class, new TypeAdapter<Instant>() {
            @Override
            public void write(JsonWriter out, Instant value) throws IOException {
                out.value(value.toString());
            }

            @Override
            public Instant read(JsonReader in) throws IOException {
                return Instant.parse(in.nextString());
            }
        }).create();
    }

}
