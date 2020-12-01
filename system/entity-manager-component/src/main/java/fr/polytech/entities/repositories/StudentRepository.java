package fr.polytech.entities.repositories;

import org.springframework.data.repository.CrudRepository;

import fr.polytech.entities.models.Student;

public interface StudentRepository extends CrudRepository<Student, Long> {
}