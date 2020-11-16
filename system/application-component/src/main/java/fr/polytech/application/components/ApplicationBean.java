package fr.polytech.application.components;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import fr.polytech.application.api.StudentAPI;
import fr.polytech.application.models.Student;
import fr.polytech.application.repositories.StudentRepository;

@Component
@ComponentScan("fr.polytech.application.repositories")
@EntityScan("fr.polytech.application.models")
@EnableJpaRepositories("fr.polytech.application.repositories")
public class ApplicationBean implements ApplicationManagement {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<Student> getApplicanteStudents() {
        return (List<Student>)studentRepository.findAll();
    }

    @Override
    public void importStudents() {
        new StudentAPI().getStudents().forEach(student -> {
            studentRepository.save(student);
        });;
    }

}
