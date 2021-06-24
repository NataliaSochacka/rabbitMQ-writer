package net.atos.writer;

import com.google.common.io.Files;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.qpid.server.Broker;
import org.apache.qpid.server.BrokerOptions;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RabbitEmbeddedBroker {

    private static final String QPID_CONFIG_LOCATION = "src/main/resources/qpid-configuration.json";
    private static final int PORT = 5672;

    private final Broker broker = new Broker();
    private final BrokerOptions brokerOptions = new BrokerOptions();


    public RabbitEmbeddedBroker() throws Exception {

        brokerOptions.setConfigProperty("qpid.amqp_port", String.valueOf(PORT));
        brokerOptions.setConfigProperty("qpid.work_dir", Files.createTempDir().getAbsolutePath());
        brokerOptions.setInitialConfigurationLocation(QPID_CONFIG_LOCATION);
        brokerOptions.setConfigurationStoreType("Memory");
        brokerOptions.setStartupLoggedToSystemOut(false);
    }

    public void start() throws Exception {

        broker.startup(brokerOptions);
    }

    public void shutdown() {

        broker.shutdown();
    }

    public ConnectionFactory createConnectionFactory() {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(PORT);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        return connectionFactory;
    }

    public CachingConnectionFactory createCachedFactory() {

        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(PORT);
        cachingConnectionFactory.setHost("localhost");
        cachingConnectionFactory.setPort(PORT);
        cachingConnectionFactory.setUsername("guest");
        cachingConnectionFactory.setPassword("guest");

        return cachingConnectionFactory;
    }

    public Channel getChannel() throws Exception {

        Connection connection = createConnectionFactory().newConnection();
        Channel channel = connection.createChannel();
        return channel;
    }
}
