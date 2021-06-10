package net.atos.writer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CSVReader {

    private int columnNumber; //?
    private String separator = ","; // z properties

    private ArrayList<File> fileList;

    @Value("#{'${list.of.folders}'.split(',')}")
    private List<String> fileStringList;

    public void importFileList(){

        fileList = new ArrayList<File>();

        for (String path : fileStringList) {

            fileList.add(new File(path));
        }
    }

    public List<Map<?, ?>> readFromFile(File inFile) {

        List<Map<?, ?>> list = null;

        try {

            CsvSchema csv = CsvSchema.emptySchema().withHeader();
            CsvMapper csvMapper = new CsvMapper();

            MappingIterator<Map<?, ?>> mappingIterator =  csvMapper.reader().forType(Map.class).with(csv).readValues(inFile);
            list = mappingIterator.readAll();

        } catch(Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public String toJSON(Map<?, ?> record){

        ObjectMapper mapper = new ObjectMapper();
        String json = null;

        try {

            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(record);
        }
        catch (JsonProcessingException e) {

            e.printStackTrace();
        }

        return json;
    }
}

