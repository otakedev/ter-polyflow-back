package fr.polytech.application.api;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.client.RestTemplate;

import fr.polytech.application.models.Student;

public class StudentAPI {

	public List<Student> getStudents() {
		RestTemplate restTemplate = new RestTemplate();
 
        // Send request with GET method and default Headers.
        List<Student> students = Arrays.asList(restTemplate.getForObject(String.format("http://%s:%s/student", Api.HOST, Api.PORT), Student[].class));

        return students;
	}
    
}
