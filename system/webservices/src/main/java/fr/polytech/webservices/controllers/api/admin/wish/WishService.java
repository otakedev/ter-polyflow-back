package fr.polytech.webservices.controllers.api.admin.wish;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.polytech.wish.components.WishManager;
import fr.polytech.webservices.Application;
import fr.polytech.course.components.CourseManager;
import fr.polytech.entities.models.Wish;

@RestController
@SpringBootApplication
@RequestMapping("/api/admin/wish")
@ComponentScan({ "fr.polytech.wish" })
public class WishService {

    @Autowired
    WishManager wm;

    @Autowired
    CourseManager cm;

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @CrossOrigin
    @GetMapping("")
    public List<Wish> getWishs() {
        log.info("GET : /api/wish");
        return wm.getWishs();
    }

    @CrossOrigin
    @PostMapping("/{studentid}")
    public Wish createWish(@PathVariable Long studentid) {
        log.info("POST : /api/wish/" + studentid);
        return wm.createWish(studentid);
    }
}