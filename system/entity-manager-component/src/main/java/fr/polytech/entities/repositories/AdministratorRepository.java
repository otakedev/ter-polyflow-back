package fr.polytech.entities.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import fr.polytech.entities.models.Administrator;

public interface AdministratorRepository extends CrudRepository<Administrator, Long> {
	Optional<Administrator> findByEmail(String email);
}