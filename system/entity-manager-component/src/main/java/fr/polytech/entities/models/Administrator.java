package fr.polytech.entities.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Administrator")
public class Administrator extends User {
    
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "occupation", length = 100, nullable = false)
    private String occupation;

    public String getOccupation() {
        return this.occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    @Override
    public Role getRole() {
		return Role.ADMIN;
	}

}