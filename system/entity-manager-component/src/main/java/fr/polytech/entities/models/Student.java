package fr.polytech.entities.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Student")
public class Student extends User {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * age: number; gender: string; currentYear: string;
     */
    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "gender", length = 100, nullable = false)
    private String gender;

    @Column(name = "currentYear", length = 100, nullable = false)
    private String currentYear;

    @OneToOne
    @JoinColumn(name = "wish_id")
    private Wish wish;

    public Wish getWish() {
        return this.wish;
    }

    public void setWish(Wish wish) {
        this.wish = wish;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCurrentYear() {
        return this.currentYear;
    }

    public void setCurrentYear(String currentYear) {
        this.currentYear = currentYear;
    }

    @Override
    public Role getRole() {
        return Role.STUDENT;
    }

}
