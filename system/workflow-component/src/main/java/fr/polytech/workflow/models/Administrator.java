package fr.polytech.workflow.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Administrator")
public class Administrator extends User {
    
    @Column(name = "occupation", length = 100, nullable = false)
    private String occupation;

    public String getOccupation() {
        return this.occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

}
