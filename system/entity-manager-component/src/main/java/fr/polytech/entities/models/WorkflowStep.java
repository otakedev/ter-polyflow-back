package fr.polytech.entities.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
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

    @Column(name = "description", length = 1000, nullable = true)
    private String description;

    @Column(name = "step_index", nullable = false)
    private int stepIndex;

    @Column(name = "externalLink", length = 100, nullable = true)
    private String externalLink;

    @ManyToMany
    @JoinTable(
        name = "workflow_step_in_charge", 
        joinColumns = { @JoinColumn(name = "workflowstep_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "administrator_id") }
    )
    private List<Administrator> personInCharge;

    @Column(name = "checkpointDate", nullable = true)
    private Date checkpointDate;

    public Date getCheckpointDate() {
        return this.checkpointDate;
    }

    public void setCheckpointDate(Date checkpointDate) {
        this.checkpointDate = checkpointDate;
    }

    public int getStepIndex() {
        return this.stepIndex;
    }

    public void setStepIndex(int stepIndex) {
        this.stepIndex = stepIndex;
    }

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
    
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof WorkflowStep)) {
            return false;
        }
        WorkflowStep workflowStep = (WorkflowStep) o;
        return Objects.equals(id, workflowStep.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}