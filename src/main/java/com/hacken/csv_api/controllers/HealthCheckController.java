package com.hacken.csv_api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/csv/health")
    public ResponseEntity<String> healthCheck() {
        return new ResponseEntity<>("Service is up and running", HttpStatus.OK);
    }
}
