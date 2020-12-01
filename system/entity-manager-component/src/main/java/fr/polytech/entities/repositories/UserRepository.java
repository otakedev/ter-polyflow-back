package fr.polytech.entities.repositories;

import org.springframework.data.repository.CrudRepository;

import fr.polytech.entities.models.User;

public interface UserRepository extends CrudRepository<User, Long> {
    public User getUserByEmail(String email);
}