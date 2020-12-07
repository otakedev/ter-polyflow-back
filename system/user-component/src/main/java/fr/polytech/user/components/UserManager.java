package fr.polytech.user.components;

import java.util.List;

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
}
