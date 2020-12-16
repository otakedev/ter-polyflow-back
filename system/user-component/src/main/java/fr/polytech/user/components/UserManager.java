package fr.polytech.user.components;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import fr.polytech.email.errors.MessageNotSentException;
import fr.polytech.entities.models.Administrator;
import fr.polytech.entities.models.Student;
import fr.polytech.entities.models.User;
import fr.polytech.entities.models.Wish;

public interface UserManager {

    List<User> getUsers();

    List<Administrator> getAdministrators();

    User getUserByEmail(String email);

    void setWish(Student student, Wish wish);

	Student getStudentById(Long studentID);

	Administrator getAdministratorById(Long objectId);

	Administrator createAdmin(String email, String firstname, String lastname, String occupation) throws MessageNotSentException;

    Administrator getAdminByEmail(String email);
    
    List<Student> upload(MultipartFile file) throws IOException;

    String download() throws IOException;
}
