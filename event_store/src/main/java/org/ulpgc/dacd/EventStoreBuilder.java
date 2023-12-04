package org.ulpgc.dacd;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;

public class EventStoreBuilder {

    private static String brokerUrl = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String topicName = "prediction.Weather";

    public ArrayList<String> start() {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(topicName);
            MessageConsumer consumer = session.createConsumer(topic);
            Message message;
            ArrayList<String> weathersList = new ArrayList<>();

            while ((message = consumer.receive(10000)) != null) {
                if (message instanceof TextMessage) {
                    String jsonEvent = ((TextMessage) message).getText();
                    weathersList.add(jsonEvent);
                }
            }
            return weathersList;

        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }

}
