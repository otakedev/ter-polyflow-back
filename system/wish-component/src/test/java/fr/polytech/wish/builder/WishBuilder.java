package fr.polytech.wish.builder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Component;

import fr.polytech.entities.models.Course;
import fr.polytech.entities.models.CourseStatus;
import fr.polytech.entities.models.CourseStudent;
import fr.polytech.entities.models.Minor;
import fr.polytech.entities.models.Wish;
import fr.polytech.entities.models.WishStatus;

@Component
public class WishBuilder {

    private Wish wish;

    public WishBuilder initBuilder() {
        this.wish = new Wish();
        this.wish.setCreationDate(new Date());
        this.wish.setExpiratioDate(new Date(new Date().getTime() + 1000l));
        this.wish.setId((long)new Random().nextInt());
        this.wish.setLastSubmitionDate(new Date());
        this.wish.setUuid(UUID.randomUUID().toString());
        this.wish.setWishStatus(WishStatus.DRAFT);
        this.wish.setCourses(new ArrayList<>());
        return this;
    }

    public WishBuilder putMinor(Minor minor) {
        this.wish.setMinor(minor);
        return this;
    }

    public WishBuilder addCourse(Course course) {
        CourseStudent cs = new CourseStudent();
        cs.setCourse(course);
        cs.setId((long)new Random().nextInt());
        cs.setStatus(CourseStatus.IN_PROGRESS);
        this.wish.getCourses().add(cs);
        return this;
    }

    public WishBuilder addCourses(List<Course> courses) {
        courses.forEach(course -> addCourse(course));
        return this;
    }

    public Wish render() {
        return this.wish;
    }
}
