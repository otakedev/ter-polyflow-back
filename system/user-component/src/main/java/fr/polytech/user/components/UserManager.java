package fr.polytech.user.components;

import java.util.List;

import fr.polytech.entities.models.User;

public interface UserManager {
    
    List<User> getUsers();

    User getUserByEmail(String email);
}
