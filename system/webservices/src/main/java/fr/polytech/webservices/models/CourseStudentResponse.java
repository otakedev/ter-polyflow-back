package fr.polytech.webservices.models;

import fr.polytech.entities.models.CourseStatus;
import fr.polytech.entities.models.CourseStudent;

public class CourseStudentResponse {
    private Long id;
    private CourseResponse course;
    private CourseStatus status;

    public CourseStudentResponse(CourseStudent courseStudent) {
        this.id = courseStudent.getId();
        this.course = new CourseResponse(courseStudent.getCourse());
        this.status = courseStudent.getStatus();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CourseResponse getCourse() {
        return this.course;
    }

    public void setCourse(CourseResponse course) {
        this.course = course;
    }

    public CourseStatus getStatus() {
        return this.status;
    }

    public void setStatus(CourseStatus status) {
        this.status = status;
    }


}
