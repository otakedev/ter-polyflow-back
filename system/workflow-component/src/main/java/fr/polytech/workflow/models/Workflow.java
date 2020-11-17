package fr.polytech.workflow.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Workflow")
public class Workflow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "description", length = 100, nullable = false)
    private String description;

    @OneToMany
    @JoinColumn(name="student_id", nullable=false)
    private Student target;

    @OneToMany
    @JoinColumn(name="author_id", nullable=false)
    private Administrator author;

    @ManyToMany
    @JoinTable(
        name = "workflow_attendees", 
        joinColumns = { @JoinColumn(name = "workflow_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "administrator_id") }
    )
    private List<Administrator> attendees;

    @Column(name = "creationDate", nullable = false)
    private Date creationDate;

    @Column(name = "deadlineDate", nullable = false)
    private Date deadlineDate;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Student getTarget() {
        return this.target;
    }

    public void setTarget(Student target) {
        this.target = target;
    }

    public Administrator getAuthor() {
        return this.author;
    }

    public void setAuthor(Administrator author) {
        this.author = author;
    }

    public List<Administrator> getAttendees() {
        return this.attendees;
    }

    public void setAttendees(List<Administrator> attendees) {
        this.attendees = attendees;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getDeadlineDate() {
        return this.deadlineDate;
    }

    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

}
