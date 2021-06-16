package net.atos.writer;

import net.atos.dto.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Writer {

    @Value("${QUEUE}")
    public Queue QUEUE;

    @Value("${EXCHANGE}")
    private String EXCHANGE;

    @Value("${ROUTING_KEY}")
    private String ROUTING_KEY;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String msg){

        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, msg);
      //  rabbitTemplate.convertAndSend(QUEUE.getName(), msg);
    }

}

