package fr.polytech.workflow.models;

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
@Table(name = "WorkflowDetails")
public class WorkflowDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "description", length = 100, nullable = false)
    private String description;

    @ManyToMany
    @JoinTable(
        name = "workflow_attendees", 
        joinColumns = { @JoinColumn(name = "workflow_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "administrator_id") }
    )
    private List<Administrator> attendees;

    @OneToMany
    @JoinColumn(name = "workflow_details_id")
    private List<WorkflowStep> steps;

    public List<WorkflowStep> getSteps() {
        return this.steps;
    }

    public void setSteps(List<WorkflowStep> steps) {
        this.steps = steps;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Administrator> getAttendees() {
        return this.attendees;
    }

    public void setAttendees(List<Administrator> attendees) {
        this.attendees = attendees;
    }
}
