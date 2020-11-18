package fr.polytech.workflow.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Workflow")
public class Workflow implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "title", length = 500, nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="student_id", nullable=false)
    private Student target;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="author_id", nullable=false)
    private Administrator author;

    @Column(name = "creationDate", nullable = false)
    private Date creationDate;

    @Column(name = "deadlineDate", nullable = false)
    private Date deadlineDate;

    @Column(name = "subject", length = 500, nullable = false)
    private String subject;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_details_id")
    private WorkflowDetails details;

    public WorkflowDetails getDetails() {
        return this.details;
    }

    public void setDetails(WorkflowDetails details) {
        this.details = details;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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
