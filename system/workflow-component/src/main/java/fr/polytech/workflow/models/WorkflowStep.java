package fr.polytech.workflow.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import javax.persistence.JoinColumn;

@Entity
@Table(name = "WorkflowStep")
public class WorkflowStep implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "title", length = 500, nullable = false)
    private String title;

    @Column(name = "description", length = 1000, nullable = false)
    private String description;

    @Column(name = "status", length = 100, nullable = false)
    private WorkflowStepStatus status;

    @Column(name = "externalLink", length = 100, nullable = false)
    private String externalLink;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "workflow_step_in_charge", 
        joinColumns = { @JoinColumn(name = "workflowstep_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "administrator_id") }
    )    
    private List<Administrator> personInCharge;

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

    public WorkflowStepStatus getStatus() {
        return this.status;
    }

    public void setStatus(WorkflowStepStatus status) {
        this.status = status;
    }

    public String getExternalLink() {
        return this.externalLink;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    public List<Administrator> getPersonInCharge() {
        return this.personInCharge;
    }

    public void setPersonInCharge(List<Administrator> personInCharge) {
        this.personInCharge = personInCharge;
    }

}