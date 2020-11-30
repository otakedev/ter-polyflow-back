package fr.polytech.user.components;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import fr.polytech.workflow.models.User;
import fr.polytech.workflow.repositories.UserRepository;

@Component
@ComponentScan("fr.polytech.workflow.repositories")
@EntityScan("fr.polytech.workflow.models")
@EnableJpaRepositories("fr.polytech.workflow.repositories")
public class UserBean implements UserManager {

    @Autowired
    UserRepository ur;

    @Override
    public List<User> getUsers() {
        return (List<User>) ur.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return ur.getUserByEmail(email);
    }
    
}
