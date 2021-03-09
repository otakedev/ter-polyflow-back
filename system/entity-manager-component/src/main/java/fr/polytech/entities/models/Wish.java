package fr.polytech.entities.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Wish")
public class Wish {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "uuid", nullable = false)
    private String uuid;
	
	@Column(name = "creationDate", nullable = false)
    private Date creationDate;
    
    @Column(name = "expirationDate", nullable = false)
    private Date expiratioDate;

    @Column(name = "lastSubmitionDate", nullable = true)
    private Date lastSubmitionDate;

    @Column(name = "wishStatus", length = 100, nullable = false)
    private WishStatus wishStatus;

    @Column(name = "minor", length = 100, nullable = true)
    private Minor minor;

    @Column(name = "sandwich_course", nullable = true)
    private Boolean sandwichCourse;

    @ManyToOne
    @JoinColumn(name = "cancelable_course_id", nullable = true)
    private Course cancelableCourse;

    @OneToMany
    @JoinColumn(name = "wish_id")
    private List<CourseStudent> courses;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<CourseStudent> getCourses() {
        return this.courses;
    }

    public void setCourses(List<CourseStudent> courses) {
        this.courses = courses;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public Course getCancelableCourse() {
        return this.cancelableCourse;
    }

    public void setCancelableCourse(Course cancelableCourse) {
        this.cancelableCourse = cancelableCourse;
    }

}
