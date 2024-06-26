package com.hacken.csv_api.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacken.csv_api.entities.Csv;
import com.hacken.csv_api.repositories.CsvRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CsvService {

    private final CsvRepository csvRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    public CsvService(CsvRepository csvRepository, ObjectMapper objectMapper) {
        this.csvRepository = csvRepository;
        this.objectMapper = objectMapper;
    }

    public List<Map<String, String>> search(String key, String value) {
        List<Csv> csvs = csvRepository.findAll();
        return csvs.stream()
                .filter(csv -> {
                    try {
                        Map<String, String> dataMap = objectMapper.readValue(
                                csv.getData(), new TypeReference<>() {
                                });
                        return value.equals(dataMap.get(key));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        return false;
                    }
                })
                .map(csv -> {
                    try {
                        return objectMapper.readValue(csv.getData(), new TypeReference<Map<String, String>>() {});
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        return null;
                    }
                }).collect(Collectors.toList());
    }

    public void processCsv(MultipartFile file, String delimiter) throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(delimiter.charAt(0)).withFirstRecordAsHeader();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVParser csvParser = new CSVParser(reader, format)) {

            for (CSVRecord csv : csvParser) {
                Map<String, String> dataMap = new HashMap<>();
                for (String header : csv.toMap().keySet()) {
                    dataMap.put(header, csv.get(header));
                }
                String jsonData = objectMapper.writeValueAsString(dataMap);
                Csv csvRecord = new Csv(jsonData);
                csvRepository.save(csvRecord);
            }
        }
    }

    public String detectDelimiter(MultipartFile file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line = reader.readLine();
            if (line != null) {
                if (line.contains(",")) {
                    return ",";
                } else if (line.contains(";")) {
                    return ";";
                } else if (line.contains("\t")) {
                    return "\t";
                }
            }
        }
        throw new IOException("Delimiter not detected");
    }
}
