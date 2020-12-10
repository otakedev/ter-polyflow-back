package fr.polytech.webservices.controllers.api.user.course;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.polytech.course.components.CourseManager;
import fr.polytech.webservices.Application;
import fr.polytech.webservices.models.CourseResponse;

@RestController
@SpringBootApplication
@RequestMapping("/api/course")
@ComponentScan({ "fr.polytech.course" })
public class CourseService {
    
    @Autowired
    CourseManager cm;

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @CrossOrigin
    @GetMapping("")
    public List<CourseResponse> getCourses() {
        log.info("GET : /api/course");
        return cm.getCourses().stream().map(e -> new CourseResponse(e)).collect(Collectors.toList());
    }
}
