package fr.polytech.workflow.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "User")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private String id;
	
	@Column(name = "firstname", length = 100, nullable = false)
	private String firstname;
	
	@Column(name = "lastname", length = 100, nullable = false)
	private String lastname;
	
	@Column(name = "profilePicUrl", length = 100, nullable = false)
	private String profilePicUrl;
	
	@Column(name = "email", length = 100, nullable = false)
    private String email;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getProfilePicUrl() {
		return this.profilePicUrl;
	}

	public void setProfilePicUrl(String profilePicUrl) {
		this.profilePicUrl = profilePicUrl;
	}


}