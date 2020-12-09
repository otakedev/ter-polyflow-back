package fr.polytech.webservices.controllers.api.user.wish;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.polytech.wish.components.WishManager;
import fr.polytech.wish.errors.WishIsNotValidException;
import fr.polytech.wish.errors.WishNotFoundException;
import fr.polytech.webservices.Application;
import fr.polytech.webservices.errors.BadRequestException;
import fr.polytech.webservices.errors.ResourceNotFoundException;
import fr.polytech.webservices.models.CoursesBody;
import fr.polytech.course.components.CourseManager;
import fr.polytech.entities.models.Minor;
import fr.polytech.entities.models.Wish;

@RestController
@SpringBootApplication
@RequestMapping("/api/wish")
@ComponentScan({ "fr.polytech.wish" })
public class WishService {

    @Autowired
    WishManager wm;

    @Autowired
    CourseManager cm;

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @CrossOrigin
    @GetMapping("/{uuid}")
    public Wish getWish(@PathVariable String uuid) {
        log.info("GET : /api/wish/" + uuid);
        try {
            return wm.getWishFromUuid(uuid);
        } catch (WishNotFoundException e) {
            throw new ResourceNotFoundException();
        }
    }

    @CrossOrigin
    @PostMapping("/{uuid}/courses")
    public Wish addCourses(@PathVariable String uuid, @RequestBody CoursesBody courses) {
        log.info("POST : /api/wish/" + uuid + "/courses");
        try {
            return wm.putWishCourses(uuid, courses.code.stream().map(coursecode -> cm.findCourseByCode(coursecode))
                    .collect(Collectors.toList()));
        } catch (WishNotFoundException e) {
            throw new ResourceNotFoundException();
        } catch (WishIsNotValidException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @CrossOrigin
    @DeleteMapping("/{uuid}/courses")
    public Wish deleteCourses(@PathVariable String uuid, @RequestBody CoursesBody courses) {
        log.info("DELETE : /api/wish/" + uuid + "/courses");
        try {
            return wm.removeWishCourses(uuid, courses.code.stream().map(coursecode -> cm.findCourseByCode(coursecode))
                    .collect(Collectors.toList()));
        } catch (WishNotFoundException e) {
            throw new ResourceNotFoundException();
        }
    }

    @CrossOrigin
    @PutMapping("/{uuid}/minor")
    public Wish putMinor(@PathVariable String uuid, @RequestBody Minor minor) {
        log.info("PUT : /api/wish/" + uuid + "/minor");
        try {
            return wm.putWishMinor(uuid, minor);
        } catch (WishNotFoundException e) {
            throw new ResourceNotFoundException();
        } catch (WishIsNotValidException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}