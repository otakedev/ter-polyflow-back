package fr.polytech.workflow.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Student")
public class Student extends User {
    /**
     * age: number;
  gender: string;
  currentYear: string;
     */
    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "gender", length = 100, nullable = false)
    private String gender;

    @Column(name = "currentYear", length = 100, nullable = false)
    private String currentYear;

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

}
