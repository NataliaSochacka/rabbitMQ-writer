package net.atos.writer;

import net.atos.dto.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Writer {

    @Value("${EXCHANGE}")
    private String EXCHANGE;

    @Value("${ROUTING_KEY}")
    private String ROUTING_KEY;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(Message msg){

        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, msg.getMessage());
       // rabbitTemplate.convertAndSend(QUEUE.getName(), msg.getMessage());
    }

}

