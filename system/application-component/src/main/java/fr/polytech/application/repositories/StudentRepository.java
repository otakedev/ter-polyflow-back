package fr.polytech.application.repositories;
import org.springframework.data.repository.CrudRepository;

import fr.polytech.application.models.Student;

public interface StudentRepository extends CrudRepository<Student, Long> {
    
}
