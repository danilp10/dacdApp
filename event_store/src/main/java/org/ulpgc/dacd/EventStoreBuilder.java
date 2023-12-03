package org.ulpgc.dacd;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.ulpgc.dacd.model.Weather;

import javax.jms.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class EventStoreBuilder {

    private static String brokerUrl = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String topicName = "prediction.Weather";

    public void start() {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(topicName);
            MessageConsumer consumer = session.createConsumer(topic);

            while (true) {
                Message message = consumer.receive();
                if (message instanceof TextMessage) {
                    String jsonEvent = ((TextMessage) message).getText();
                    Weather weatherEvent = deserializeWeatherEvent(jsonEvent);
                    storeEvent(weatherEvent);
                }
            }
        } catch (JMSException | IOException e) {
            e.printStackTrace();
        }
    }

    private Weather deserializeWeatherEvent(String jsonEvent) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonEvent, Weather.class);
    }

    private void storeEvent(Weather event) throws IOException {
        String ss = event.getSs();
        Instant ts = event.getTs();
        String formattedDate = formatInstantDate(ts);
        String directoryPath = "eventstore/" + topicName + "/" + ss + "/";
        String filePath = directoryPath + formattedDate + ".events";

        new java.io.File(directoryPath).mkdirs();

        try (FileWriter writer = new FileWriter(filePath, true)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonEvent = gson.toJson(event);
            writer.write(jsonEvent);
            writer.write(System.lineSeparator());
        }
    }

    private String formatInstantDate(Instant instant) {
        LocalDate localDate = LocalDate.ofInstant(instant, ZoneOffset.UTC);
        return localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public static void main(String[] args) {
        EventStoreBuilder eventStoreBuilder = new EventStoreBuilder();
        eventStoreBuilder.start();
    }
}
