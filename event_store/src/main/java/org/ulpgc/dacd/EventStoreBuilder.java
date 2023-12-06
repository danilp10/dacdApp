package org.ulpgc.dacd;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class EventStoreBuilder {

    private static String brokerUrl = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String topicName = "prediction.Weather";
    private static String subscriptionName = "DurableSubscription";
    private static JsonStore jsonStore = new JsonStore();

    public void start() {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
            Connection connection = connectionFactory.createConnection();
            connection.setClientID("EventStoreBuilder");
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(topicName);
            MessageConsumer consumer = session.createDurableSubscriber(topic, subscriptionName);
            consumer.setMessageListener(message -> {
                try {
                    jsonStore.storeEvent(((TextMessage) message).getText());
                } catch (IOException | JMSException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
