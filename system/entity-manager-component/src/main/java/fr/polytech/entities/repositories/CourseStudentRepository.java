package fr.polytech.entities.repositories;

import org.springframework.data.repository.CrudRepository;

import fr.polytech.entities.models.CourseStudent;

public interface CourseStudentRepository extends CrudRepository<CourseStudent, Long> {
}
