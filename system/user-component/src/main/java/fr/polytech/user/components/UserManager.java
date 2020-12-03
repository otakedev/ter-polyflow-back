package fr.polytech.user.components;

import java.util.List;

import fr.polytech.entities.models.Student;
import fr.polytech.entities.models.User;
import fr.polytech.entities.models.Wish;

public interface UserManager {
    
    List<User> getUsers();

    User getUserByEmail(String email);

    void setWish(Student student, Wish wish);

	Student getStudentById(Long studentID);
}
