package fr.polytech.webservices.models;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import fr.polytech.entities.models.Minor;
import fr.polytech.entities.models.Wish;
import fr.polytech.entities.models.WishStatus;

public class WishResponse {
    private Long id;
    private String uuid;
    private Date creationDate;
    private Date expiratioDate;
    private Date lastSubmitionDate;
    private WishStatus wishStatus;
    private Minor minor;
    private Boolean sandwichCourse;
    private CourseResponse cancelableCourse;
    private List<CourseStudentResponse> courses;

    public WishResponse(Wish wish) {
        this.id = wish.getId();
        this.uuid = wish.getUuid();
        this.creationDate = wish.getCreationDate();
        this.expiratioDate = wish.getExpiratioDate();
        this.lastSubmitionDate = wish.getLastSubmitionDate();
        this.wishStatus = wish.getWishStatus();
        this.minor = wish.getMinor();
        this.sandwichCourse = wish.getSandwichCourse();
        this.cancelableCourse = wish.getCancelableCourse() == null ? null : new CourseResponse(wish.getCancelableCourse());
        this.courses = wish.getCourses().stream().map(e -> new CourseStudentResponse(e)).collect(Collectors.toList());
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getExpiratioDate() {
        return this.expiratioDate;
    }

    public void setExpiratioDate(Date expiratioDate) {
        this.expiratioDate = expiratioDate;
    }

    public Date getLastSubmitionDate() {
        return this.lastSubmitionDate;
    }

    public void setLastSubmitionDate(Date lastSubmitionDate) {
        this.lastSubmitionDate = lastSubmitionDate;
    }

    public WishStatus getWishStatus() {
        return this.wishStatus;
    }

    public void setWishStatus(WishStatus wishStatus) {
        this.wishStatus = wishStatus;
    }

    public Minor getMinor() {
        return this.minor;
    }

    public void setMinor(Minor minor) {
        this.minor = minor;
    }

    public Boolean isSandwichCourse() {
        return this.sandwichCourse;
    }

    public Boolean getSandwichCourse() {
        return this.sandwichCourse;
    }

    public void setSandwichCourse(Boolean sandwichCourse) {
        this.sandwichCourse = sandwichCourse;
    }

    public CourseResponse getCancelableCourse() {
        return this.cancelableCourse;
    }

    public void setCancelableCourse(CourseResponse cancelableCourse) {
        this.cancelableCourse = cancelableCourse;
    }

    public List<CourseStudentResponse> getCourses() {
        return this.courses;
    }

    public void setCourses(List<CourseStudentResponse> courses) {
        this.courses = courses;
    }
    
}
