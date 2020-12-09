package fr.polytech.entities.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.InheritanceType;

@Entity
@Table(name = "Course")
@Inheritance(strategy = InheritanceType.JOINED)
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "period", nullable = false)
    private Period period;

    @Column(name = "dayOfTheWeek", nullable = false)
    private int dayOfTheWeek;

    @Column(name = "code", length = 100, nullable = false)
    private String code;

    @Column(name = "description", length = 500, nullable = false)
    private String description;

    @Column(name = "halfday", nullable = false)
    private HalfDay halfDay;

    @ManyToMany
    @JoinTable(
        name = "course_constraints", 
        joinColumns = { @JoinColumn(name = "course_first_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "course_second_id") }
    )
    private List<Course> constraints;

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

    public List<Course> getConstraints() {
        return this.constraints;
    }

    public void setConstraints(List<Course> constraints) {
        this.constraints = constraints;
    }

    public Minor getMinor() {
        return null;
    }

}
