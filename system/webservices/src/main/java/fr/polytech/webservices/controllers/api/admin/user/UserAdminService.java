package fr.polytech.webservices.controllers.api.admin.user;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fr.polytech.user.components.UserManager;
import fr.polytech.webservices.Application;
import fr.polytech.webservices.errors.BadRequestException;
import fr.polytech.webservices.models.AdministratorBody;
import fr.polytech.email.errors.MessageNotSentException;
import fr.polytech.entities.models.Administrator;
import fr.polytech.entities.models.Student;
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

    @CrossOrigin
    @PostMapping("")
    public List<Student> importCSV(@RequestPart("file") MultipartFile file) {
        log.info("POST : /api/admin/user");
        try {
            return um.upload(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BadRequestException();
        }
    }

    @CrossOrigin
    @GetMapping(path = "/export", produces = "text/csv; charset=UTF-8")
    public ResponseEntity<String> export() throws IOException {
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8")
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"students.csv\"")
                    .body(um.download());
    }
}