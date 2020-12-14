package fr.polytech.webservices.controllers.api.admin.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.polytech.user.components.UserManager;
import fr.polytech.webservices.Application;
import fr.polytech.webservices.errors.BadRequestException;
import fr.polytech.webservices.models.AdministratorBody;
import fr.polytech.email.errors.MessageNotSentException;
import fr.polytech.entities.models.Administrator;
import fr.polytech.entities.models.User;

@RestController
@CrossOrigin
@RequestMapping("/api/admin/user")
@ComponentScan({ "fr.polytech.user" })
public class UserAdminService {

    @Autowired
    UserManager um;

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @GetMapping("")
    public List<User> getUsers() {
        log.info("GET : /api/user");
        return um.getUsers();
    }

    @GetMapping("/admin")
    public List<Administrator> getAdmins() {
        log.info("GET : /api/user/admin");
        return um.getAdministrators();
    }

    @CrossOrigin
    @PostMapping("/admin")
    public Administrator createAdmin(@RequestBody AdministratorBody admin) {
        log.info("POST : /api/user/admin");
        try {
            return um.createAdmin(admin.getEmail(), admin.getFirstname(), admin.getLastname(), admin.getOccupation());
        } catch (MessageNotSentException e) {
            throw new BadRequestException();
        }
    }

}
