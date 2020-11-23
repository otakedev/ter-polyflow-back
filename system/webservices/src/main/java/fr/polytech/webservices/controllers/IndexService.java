package fr.polytech.webservices.controllers;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@SpringBootApplication
public class IndexService {
    
    @CrossOrigin
    @GetMapping("")
    public String index() {
        return "index";
    }
}