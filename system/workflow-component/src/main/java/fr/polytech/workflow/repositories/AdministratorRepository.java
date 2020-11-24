package fr.polytech.workflow.repositories;

import org.springframework.data.repository.CrudRepository;

import fr.polytech.workflow.models.Administrator;

public interface AdministratorRepository extends CrudRepository<Administrator, Long> {
}