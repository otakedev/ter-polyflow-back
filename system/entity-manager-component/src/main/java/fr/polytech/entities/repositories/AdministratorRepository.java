package fr.polytech.entities.repositories;

import org.springframework.data.repository.CrudRepository;

import fr.polytech.entities.models.Administrator;

public interface AdministratorRepository extends CrudRepository<Administrator, Long> {
}