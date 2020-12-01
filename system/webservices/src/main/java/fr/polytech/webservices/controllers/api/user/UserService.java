package fr.polytech.webservices.controllers.api.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.polytech.user.components.UserManager;
import fr.polytech.webservices.Application;
import fr.polytech.entities.models.User;

@RestController
@SpringBootApplication
@RequestMapping("/api/user")
@ComponentScan({ "fr.polytech.user" })
public class UserService {
    
    @Autowired
    UserManager um;

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @CrossOrigin
    @GetMapping("")
    public List<User> getUsers() {
        log.info("GET : /api/user");
        return um.getUsers();
    }
}
