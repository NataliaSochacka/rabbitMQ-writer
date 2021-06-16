package net.atos.writer;

import net.atos.dto.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@EnableScheduling
public class Controller {

    Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private Writer writer;

    @Value("${main.directory}")
    private String mainDirectory;

    @Value("#{'${list.of.folders}'.split(',')}")
    private ArrayList<String> folderStringList;

    @Scheduled(fixedDelayString = "${SEND_INTERVAL}")
    public void writeMessage(){

        System.out.println(folderStringList.toString());

       // int batchSize = 2;
        CSVReader csvReader = new CSVReader();

        for (String folderName : folderStringList) {

            List<File> fileList = csvReader.getFilesFromFolder(mainDirectory + folderName);

            for (File file : fileList) {

                String msg = csvReader.toJSON(csvReader.readFromFile(file)); // poki co wiadomoscia caly plik
                this.writer.sendMessage(msg);
                logger.info("Sent: " + msg);
            }

        }
    }
}
