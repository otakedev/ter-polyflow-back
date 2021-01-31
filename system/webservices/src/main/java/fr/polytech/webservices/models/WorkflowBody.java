package fr.polytech.webservices.models;

import java.io.Serializable;
import java.util.Date;

import fr.polytech.entities.models.WorkflowStatus;

public class WorkflowBody implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1061188687762451154L;
    private String title;
    private Long target;
    private Long author;
    private Date creationDate;
    private Date deadlineDate;
    private String subject;
    private String currentStep;
    private WorkflowStatus status;
    private boolean isUrgent;

    public WorkflowStatus getStatus() {
        return this.status;
    }

    public void setStatus(WorkflowStatus status) {
        this.status = status;
    }

    public String getCurrentStep() {
        return this.currentStep;
    }

    public void setCurrentStep(String currentStep) {
        this.currentStep = currentStep;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTarget() {
        return this.target;
    }

    public void setTarget(Long target) {
        this.target = target;
    }

    public Long getAuthor() {
        return this.author;
    }

    public void setAuthor(Long author) {
        this.author = author;
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

    public boolean getIsUrgent() {
        return isUrgent;
    }

    public void setIsUrgent(boolean isUrgent) {
        this.isUrgent = isUrgent;
    }
}
