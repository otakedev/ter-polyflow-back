package fr.polytech.webservices.models;

import java.util.List;
import java.util.stream.Collectors;

import fr.polytech.entities.models.Course;
import fr.polytech.entities.models.HalfDay;
import fr.polytech.entities.models.Minor;
import fr.polytech.entities.models.Period;

public class CourseResponse {
    private Long id;
    private Period period;
    private int dayOfTheWeek;
    private String code;
    private String description;
    private HalfDay halfDay;
    private List<String> constraints;
    private Minor minor;

    public CourseResponse(Course course) {
        this.id = course.getId();
        this.period = course.getPeriod();
        this.dayOfTheWeek = course.getDayOfTheWeek();
        this.code = course.getCode();
        this.description = course.getDescription();
        this.halfDay = course.getHalfDay();
        this.constraints = course.getConstraints().stream().map(Course::getCode).collect(Collectors.toList());
        this.minor = course.getMinor();
    }

    public HalfDay getHalfDay() {
        return this.halfDay;
    }

    public void setHalfDay(HalfDay halfDay) {
        this.halfDay = halfDay;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Period getPeriod() {
        return this.period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public int getDayOfTheWeek() {
        return this.dayOfTheWeek;
    }

    public void setDayOfTheWeek(int dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getConstraints() {
        return this.constraints;
    }

    public void setConstraints(List<String> constraints) {
        this.constraints = constraints;
    }

    public Minor getMinor() {
        return this.minor;
    }

    public void setMinor(Minor minor) {
        this.minor = minor;
    }

}
