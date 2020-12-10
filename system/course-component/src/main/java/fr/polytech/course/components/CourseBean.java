package fr.polytech.course.components;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import fr.polytech.course.errors.CourseNotFoundException;
import fr.polytech.entities.models.Course;
import fr.polytech.entities.models.CourseStatus;
import fr.polytech.entities.models.CourseStudent;
import fr.polytech.entities.models.Minor;
import fr.polytech.entities.models.MinorCourse;
import fr.polytech.entities.repositories.CourseRepository;
import fr.polytech.entities.repositories.CourseStudentRepository;
import fr.polytech.entities.repositories.MinorCourseRepository;

@Component
@ComponentScan("fr.polytech.entities.repositories")
@EntityScan("fr.polytech.entities.models")
@EnableJpaRepositories("fr.polytech.entities.repositories")
public class CourseBean implements CourseManager {

    @Autowired
    CourseRepository cr;

    @Autowired
    MinorCourseRepository mcr;

    @Autowired
    CourseStudentRepository csr;

    @Override
    public List<Course> getCourses() {
        return (List<Course>) cr.findAll();
    }

    public Course findCourseByCode(String code) {
        return cr.getCourseByCode(code);
    }

    @Override
    public boolean coursesAreCompatible(Course c1, Course c2) {
        if (c1 == null || c2 == null)
            return false;
        return !(c1.getPeriod() == c2.getPeriod() && // Same period
                c1.getDayOfTheWeek() == c2.getDayOfTheWeek() && c1.getHalfDay() == c2.getHalfDay());
    }

    @Override
    public boolean courseAreCompatibleWithList(List<Course> courses, Course next) {
        for (Course course : courses) {
            if (!coursesAreCompatible(course, next))
                return false;
        }
        return true;
    }

    @Override
    public List<MinorCourse> getCoursesWithMinor(Minor minor) {
        return mcr.findCourseByMinor(minor);
    }

    @Override
    public CourseStudent createCourseStudent(Course course) {
        CourseStudent courseStudent = new CourseStudent();
        courseStudent.setCourse(course);
        courseStudent.setStatus(CourseStatus.IN_PROGRESS);
        csr.save(courseStudent);
        return courseStudent;
    }

    @Override
    public Course getCourseByCode(String code) throws CourseNotFoundException {
        Course course = findCourseByCode(code);
        if(course == null) throw new CourseNotFoundException();
        return course;
    }

    
}
