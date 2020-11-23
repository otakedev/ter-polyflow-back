package fr.polytech.workflow.repositories;

import org.springframework.data.repository.CrudRepository;

import fr.polytech.workflow.models.Student;

public interface StudentRepository extends CrudRepository<Student, Long> {
}