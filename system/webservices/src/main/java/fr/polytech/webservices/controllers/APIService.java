package fr.polytech.webservices.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class APIService {

    @Value("${env.mode}")
	private String env;

    class Success {
        public String status = "success";
    }
    
    @CrossOrigin
    @GetMapping("/api")
    public Success api() {
        return new Success();
    }

    @CrossOrigin
    @GetMapping("/env")
    public String env() {
        return this.env;
    }
}