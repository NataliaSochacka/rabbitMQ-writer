package net.atos.writer;

import com.rabbitmq.client.Channel;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitIntegrationTest {

    @ClassRule
    public static RabbitEmbeddedBrokerRule rabbitEmbeddedBrokerRule = new RabbitEmbeddedBrokerRule();

    @Autowired
    public RabbitEmbeddedBroker rabbitEmbeddedBroker;

    String EXCHANGE = "test_exchange";
    String ROUTING_KEY = "test_routing_key";

    @Test
    public void channelTest() throws Exception {

        Channel channel = rabbitEmbeddedBroker.getChannel();

        assertNotNull(channel.getConnection());
    }

    @Test
    public void messageSendingTest(){

        sendMessage("test");
    }

    private void createExchange(String id) {

        final RabbitAdmin admin = new RabbitAdmin(rabbitEmbeddedBroker.createCachedFactory());
        TopicExchange topicExchange = new TopicExchange(EXCHANGE);
        admin.declareExchange(topicExchange);
    }

    private void deleteExchange() {

        final RabbitAdmin admin = new RabbitAdmin(rabbitEmbeddedBroker.createCachedFactory());
        admin.deleteExchange(EXCHANGE);
    }

    private void sendMessage(String message) {

        final RabbitTemplate rabbitTemplate = new RabbitTemplate(rabbitEmbeddedBroker.createCachedFactory());

        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, message);
    }

}
