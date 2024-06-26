package com.hacken.csv_api.controllers;

import com.hacken.csv_api.services.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/csv")
public class CsvController {

    private final CsvService csvService;

    @Autowired
    public CsvController(CsvService csvService) {
        this.csvService = csvService;
    }

    @PostMapping("/upload")
    public String uploadCsv(@RequestParam("file") MultipartFile file) {
        try {
            String delimiter = csvService.detectDelimiter(file);
            csvService.processCsv(file, delimiter);
            return "File uploaded and processed successfully.";
        } catch (IOException ex) {
            return "Failed to process the file: " + ex.getMessage();
        }
    }
}
