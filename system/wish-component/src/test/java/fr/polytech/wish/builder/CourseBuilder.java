package fr.polytech.wish.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import fr.polytech.entities.models.Course;
import fr.polytech.entities.models.HalfDay;
import fr.polytech.entities.models.Minor;
import fr.polytech.entities.models.MinorCourse;
import fr.polytech.entities.models.Period;

@Component
public class CourseBuilder {

    private List<Course> courses;
    
    private Course createCourse(String name, Period period, int dayOfTheWeek, HalfDay halfDay) {
        Course c1 = new Course();
        c1.setCode(UUID.randomUUID().toString());
        c1.setContraints(new ArrayList<>());
        c1.setDayOfTheWeek(dayOfTheWeek);
        c1.setDescription(name);
        c1.setHalfDay(halfDay);
        c1.setId((long)new Random().nextInt());
        c1.setPeriod(period);
        return c1;
    }

    private MinorCourse createMinorCourse(Minor minor, String name, Period period, int dayOfTheWeek, HalfDay halfDay) {
        MinorCourse c1 = new MinorCourse();
        c1.setCode(UUID.randomUUID().toString());
        c1.setContraints(new ArrayList<>());
        c1.setDayOfTheWeek(dayOfTheWeek);
        c1.setDescription(name);
        c1.setHalfDay(halfDay);
        c1.setId((long)new Random().nextInt());
        c1.setPeriod(period);
        c1.setMinor(minor);
        return c1;
    }

    private void contraint(Course c1, Course c2) {
        c1.getContraints().add(c2);
        c2.getContraints().add(c1);
    }

    private Course getCourseByName(String name) {
        List<Course> courses = this.courses.stream().filter(e -> e.getDescription().equals(name)).collect(Collectors.toList());
        if(courses.isEmpty()) return null;
        return courses.get(0);
    } 

    public CourseBuilder initBuilder() {
        this.courses = new ArrayList<>();
        return this;
    }

    public CourseBuilder addCourse(String name, Period period, int dayOfTheWeek, HalfDay halfDay) {
        this.courses.add(createCourse(name, period, dayOfTheWeek, halfDay));
        return this;
    }

    public CourseBuilder addCourse(String name, Period period, int dayOfTheWeek, HalfDay halfDay, Minor minor) {
        this.courses.add(createMinorCourse(minor, name, period, dayOfTheWeek, halfDay));
        return this;
    }

    public CourseBuilder contraint(String n1, String n2) {
        contraint(getCourseByName(n1), getCourseByName(n2));
        return this;
    }

    public List<Course> render() {
        return this.courses;
    }
}
