package fr.polytech.entities.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "MinorCourse")
public class MinorCourse extends Course {

    @Column(name = "minor", nullable = false)
    private Minor minor;

    @Override
    public Minor getMinor() {
        return this.minor;
    }

    public void setMinor(Minor minor) {
        this.minor = minor;
    }

}
