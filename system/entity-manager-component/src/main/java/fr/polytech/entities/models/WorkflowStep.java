package fr.polytech.entities.models;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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

    @Column(name = "comment", length = 500, nullable = true)
    private String comment;

    @Column(name = "externalLink", length = 100, nullable = true)
    private String externalLink;

    @ManyToOne
    @JoinColumn(name = "administrator_id", nullable = false)
    private Administrator personInCharge;

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

    public Administrator getPersonInCharge() {
        return this.personInCharge;
    }

    public void setPersonInCharge(Administrator personInCharge) {
        this.personInCharge = personInCharge;
    }

    public WorkflowStep() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public WorkflowStep(Long id, String title, String description, int stepIndex, String externalLink,
            Administrator personInCharge, Date checkpointDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.stepIndex = stepIndex;
        this.externalLink = externalLink;
        this.personInCharge = personInCharge;
        this.checkpointDate = checkpointDate;
    }

    public WorkflowStep id(Long id) {
        setId(id);
        return this;
    }

    public WorkflowStep title(String title) {
        setTitle(title);
        return this;
    }

    public WorkflowStep description(String description) {
        setDescription(description);
        return this;
    }

    public WorkflowStep stepIndex(int stepIndex) {
        setStepIndex(stepIndex);
        return this;
    }

    public WorkflowStep externalLink(String externalLink) {
        setExternalLink(externalLink);
        return this;
    }

    public WorkflowStep personInCharge(Administrator personInCharge) {
        setPersonInCharge(personInCharge);
        return this;
    }

    public WorkflowStep checkpointDate(Date checkpointDate) {
        setCheckpointDate(checkpointDate);
        return this;
    }

    @Override
    public String toString() {
        return "{" + " id='" + getId() + "'" + ", title='" + getTitle() + "'" + ", description='" + getDescription()
                + "'" + ", stepIndex='" + getStepIndex() + "'" + ", externalLink='" + getExternalLink() + "'"
                + ", personInCharge='" + getPersonInCharge() + "'" + ", checkpointDate='" + getCheckpointDate() + "'"
                + "}";
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
