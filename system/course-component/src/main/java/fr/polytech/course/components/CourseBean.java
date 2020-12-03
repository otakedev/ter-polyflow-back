package fr.polytech.course.components;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import fr.polytech.entities.models.Course;
import fr.polytech.entities.repositories.CourseRepository;

@Component
@ComponentScan("fr.polytech.entities.repositories")
@EntityScan("fr.polytech.entities.models")
@EnableJpaRepositories("fr.polytech.entities.repositories")
public class CourseBean implements CourseManager {

    @Autowired
    CourseRepository ur;

    @Override
    public List<Course> getCourses() {
        return (List<Course>) ur.findAll();
    }
    
}
