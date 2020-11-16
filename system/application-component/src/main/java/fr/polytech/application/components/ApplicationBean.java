package fr.polytech.application.components;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import fr.polytech.application.models.Student;

@Component
@ComponentScan("fr.polytech.application.repositories")
@EntityScan("fr.polytech.application.models")
@EnableJpaRepositories("fr.polytech.application.repositories")
public class ApplicationBean implements ApplicationManagement {

    @Override
    public List<Student> getApplicanteStudents() {
        return new ArrayList<>();
    }

}
