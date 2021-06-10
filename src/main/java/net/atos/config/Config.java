package net.atos.config;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class Config {

    @Value("${QUEUE}")
    public String QUEUE;

    @Value("${EXCHANGE}")
    private String EXCHANGE;

    @Value("${ROUTING_KEY}")
    private String ROUTING_KEY ;

    @Bean
    public RabbitAdmin rabbitAdmin(Queue queue, ConnectionFactory connectionFactory) {
        final TopicExchange exchange = new TopicExchange(EXCHANGE, true, false);

        final RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.declareQueue(queue);
        admin.declareExchange(exchange);
        admin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY));

        return admin;
    }

    @Bean
    public Queue queue(){

        return new Queue(QUEUE, true, false, false);
    }

    @Bean
    public TopicExchange exchange(){

        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange){

        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter converter(){

        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory){

        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());

        return rabbitTemplate;
    }
}
