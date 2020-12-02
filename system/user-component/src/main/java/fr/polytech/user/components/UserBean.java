package fr.polytech.user.components;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import fr.polytech.entities.models.Student;
import fr.polytech.entities.models.User;
import fr.polytech.entities.models.Wish;
import fr.polytech.entities.repositories.StudentRepository;
import fr.polytech.entities.repositories.UserRepository;

@Component
@ComponentScan("fr.polytech.entities.repositories")
@EntityScan("fr.polytech.entities.models")
@EnableJpaRepositories("fr.polytech.entities.repositories")
public class UserBean implements UserManager {

    @Autowired
    UserRepository ur;

    @Autowired
    StudentRepository sr;

    @Override
    public List<User> getUsers() {
        return (List<User>) ur.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return ur.getUserByEmail(email);
    }

    public void setWish(Long studentID, Wish wish) {
        Optional<Student> student = sr.findById(studentID);
        if(student.isPresent())
        {
            student.get().setWish(wish);
            sr.save(student.get());
        }
    }
    
}
