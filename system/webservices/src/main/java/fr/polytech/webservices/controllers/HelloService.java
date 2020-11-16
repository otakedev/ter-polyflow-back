package fr.polytech.webservices.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.polytech.hello.components.IHello;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@SpringBootApplication
@ComponentScan({ "fr.polytech.hello" })
public class HelloService {

    @Autowired
    IHello hello;

    @CrossOrigin
    @GetMapping("/hello")
    public ResponseEntity<String> getMishaps() {
        String msgHello = hello.hello();
        return ok().body(msgHello);
    }
    
}
