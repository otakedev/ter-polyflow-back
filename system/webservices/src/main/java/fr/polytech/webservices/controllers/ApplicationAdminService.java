package fr.polytech.webservices.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.polytech.application.components.ApplicationManagement;
import fr.polytech.application.models.Student;

import static org.springframework.http.ResponseEntity.ok;

import java.util.List;

@RestController
@SpringBootApplication
@ComponentScan({ "fr.polytech.application" })
@RequestMapping("/api/admin")
public class ApplicationAdminService {

    @Autowired
    ApplicationManagement applicationManagement;

    @CrossOrigin
    @GetMapping("/student")
    public ResponseEntity<List<Student>> getMishaps() {
        List<Student> students = applicationManagement.getApplicanteStudents();
        return ok().body(students);
    }
    
}
