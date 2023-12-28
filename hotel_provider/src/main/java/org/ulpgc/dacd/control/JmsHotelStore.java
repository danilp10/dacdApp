package org.ulpgc.dacd.control;

import javax.jms.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.ulpgc.dacd.model.Hotel;
import java.io.IOException;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class JmsHotelStore implements HotelStore {

    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String subject = "rate.Hotel";
    private Set<String> sentKeys = new HashSet<>();

    public void save(Hotel hotel) {
        try {
            if (!containsKey(hotel.getKey())) {
                ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
                Connection connection = connectionFactory.createConnection();
                connection.start();

                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Topic topic = session.createTopic(subject);
                MessageProducer producer = session.createProducer(topic);

                String jsonHotel = prepareGson().toJson(hotel);
                System.out.println("Serialized Hotel Event: " + jsonHotel);
                TextMessage message = session.createTextMessage(jsonHotel);
                producer.send(message);

                System.out.println("Hotel data sent to the queue.");

                sentKeys.add(hotel.getKey());

                connection.close();
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean containsKey(String key) {
        return sentKeys.contains(key);
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
