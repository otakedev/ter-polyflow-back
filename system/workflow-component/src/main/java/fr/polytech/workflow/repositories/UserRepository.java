package fr.polytech.workflow.repositories;

import org.springframework.data.repository.CrudRepository;

import fr.polytech.workflow.models.User;

public interface UserRepository extends CrudRepository<User, Long> {
    public User getUserByEmail(String email);
}