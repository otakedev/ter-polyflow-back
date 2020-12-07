package fr.polytech.course.components;

import java.util.List;

import fr.polytech.entities.models.Course;
import fr.polytech.entities.models.CourseStudent;
import fr.polytech.entities.models.Minor;
import fr.polytech.entities.models.MinorCourse;

public interface CourseManager {
    
    List<Course> getCourses();

    Course findCourseByCode(String code);

    boolean coursesAreCompatible(Course c1, Course c2);

    boolean courseAreCompatibleWithList(List<Course> courses, Course next);

    CourseStudent createCourseStudent(Course course);

    List<MinorCourse> getCoursesWithMinor(Minor minor);
}
