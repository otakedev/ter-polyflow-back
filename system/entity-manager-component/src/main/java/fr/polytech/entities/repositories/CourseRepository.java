package fr.polytech.entities.repositories;

import org.springframework.data.repository.CrudRepository;

import fr.polytech.entities.models.Course;

public interface CourseRepository extends CrudRepository<Course, Long> {

	Course getCourseByCode(String code);
}
