package com.hacken.csv_api.controllers;

import com.hacken.csv_api.services.CsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/csv")
@Tag(name = "CSV API", description = "API to upload and search the CSV files.")
public class CsvController {

    private final CsvService csvService;

    @Autowired
    public CsvController(CsvService csvService) {
        this.csvService = csvService;
    }

    @Operation(summary = "Search CSV file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found the file",
                content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = Map.class))}),
            @ApiResponse(responseCode = "400", description = "The fields entered do not match the query or are invalid",
                content = @Content),
            @ApiResponse(responseCode = "500", description = "An error occurred during the query, check the logs",
                content = @Content)
    })
    @GetMapping("/search")
    public List<Map<String, String>> search(@RequestParam String key, @RequestParam String value) {
        return csvService.search(key, value);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully uploaded and processed the file",
                content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "The csv file inserted is not valid",
                content = {@Content}),
            @ApiResponse(responseCode = "500", description = "An error occurred during the uploading or processing, check the logs",
                content = {@Content})
    })
    @Operation(summary = "Upload CSV file")
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
