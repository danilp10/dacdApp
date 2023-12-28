package org.ulpgc.dacd;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class EventStoreBuilder {

    private static String brokerUrl = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String topicWeatherName = "prediction.Weather";
    private static String topicHotelName = "rate.Hotel";
    private static String subscriptionWeatherName = "DurableWeatherSubscription";
    private static String subscriptionHotelName = "DurableHotelSubscription";
    private static JsonStore jsonWeatherStore = new JsonStore(topicWeatherName, "event_store");
    private static JsonStore jsonHotelStore = new JsonStore(topicHotelName, "event_store");

    public void start() {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
            Connection connection = connectionFactory.createConnection();
            connection.setClientID("EventStoreBuilder");
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic weatherTopic = session.createTopic(topicWeatherName);
            Topic hotelTopic = session.createTopic(topicHotelName);
            MessageConsumer weatherConsumer = session.createDurableSubscriber(weatherTopic, subscriptionWeatherName);
            MessageConsumer hotelConsumer = session.createDurableSubscriber(hotelTopic, subscriptionHotelName);
            weatherConsumer.setMessageListener(message -> {
                try {
                    jsonWeatherStore.storeEvent(((TextMessage) message).getText());
                } catch (IOException | JMSException e) {
                    throw new RuntimeException(e);
                }
            });
            hotelConsumer.setMessageListener(message -> {
                try {
                    jsonHotelStore.storeEvent(((TextMessage) message).getText());
                } catch (IOException | JMSException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
