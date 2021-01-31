package fr.polytech.webservices.models;

import java.util.List;

import fr.polytech.entities.models.File;

public class WorkflowDetailsBody {

    private Long id;
    private String description;
    private List<Long> steps;
    private List<Long> attendees;
    private List<File> files;
    private String statusComment;

    public List<File> getFiles() {
        return this.files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Long> getSteps() {
        return this.steps;
    }

    public void setSteps(List<Long> steps) {
        this.steps = steps;
    }

    public List<Long> getAttendees() {
        return this.attendees;
    }

    public void setAttendees(List<Long> attendees) {
        this.attendees = attendees;
    }

    public String getStatusComment() {
        return statusComment;
    }

    public void setStatusComment(String statusComment) {
        this.statusComment = statusComment;
    }

}
