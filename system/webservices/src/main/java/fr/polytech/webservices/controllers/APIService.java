package fr.polytech.webservices.controllers;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class APIService {

    class Success {
        public String status = "success";
    }
    
    @CrossOrigin
    @GetMapping("/api")
    public Success api() {
        return new Success();
    }
}