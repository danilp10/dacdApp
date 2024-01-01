package org.ulpgc.dacd;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class EventStoreBuilder {
    private static final String brokerUrl = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static final String topicWeatherName = "prediction.Weather";
    private static final String topicHotelName = "rate.Hotel";
    private static final String subscriptionWeatherName = "DurableWeatherSubscription";
    private static final String subscriptionHotelName = "DurableHotelSubscription";
    private JsonStore jsonWeatherStore;
    private JsonStore jsonHotelStore;

    public EventStoreBuilder(String rootDirectory) {
        jsonWeatherStore = new JsonStore(topicWeatherName, rootDirectory);
        jsonHotelStore = new JsonStore(topicHotelName, rootDirectory);
    }


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
