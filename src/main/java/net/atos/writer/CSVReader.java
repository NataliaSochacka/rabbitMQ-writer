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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
//@NoArgsConstructor
@ToString
public class CSVReader {

    public List<File> getFilesFromFolder(String folderName) {

        List<File> fileList = null;

        try  {

            fileList = Files.walk(Paths.get(folderName))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e){

            e.printStackTrace();
        }

        return fileList;
    }

    public List<Map<?, ?>> readFromFile(File inFile) {

        List<Map<?, ?>> list = null;

        try {

            CsvSchema csv = CsvSchema.emptySchema().withHeader();
            CsvMapper csvMapper = new CsvMapper();

            MappingIterator<Map<?, ?>> mappingIterator =  csvMapper.reader().forType(Map.class).with(csv).readValues(inFile);

            //Tu po batch.size linijek
            list = mappingIterator.readAll();

        } catch(Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public String toJSON(List<Map<?, ?>> records){

        ObjectMapper mapper = new ObjectMapper();
        String json = "";

        for (Map<?, ?> record : records) {
            try {

                json += mapper.writerWithDefaultPrettyPrinter().writeValueAsString(record);
            } catch (JsonProcessingException e) {

                e.printStackTrace();
            }
        }

        return json;
    }
}

