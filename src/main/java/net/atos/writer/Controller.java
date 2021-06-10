package net.atos.writer;

import net.atos.dto.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class Controller {

    Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private Writer writer;

    @Scheduled(fixedDelayString = "${SEND_INTERVAL}")
    public void writeMessage(){

        Message msg = new Message(1, "test");

        this.writer.sendMessage(msg);
        logger.info("Sent: " + msg.getMessageID() + ", " + msg.getMessage());
    }
}
