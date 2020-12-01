package fr.polytech.entities.repositories;

import org.springframework.data.repository.CrudRepository;

import fr.polytech.entities.models.WorkflowDetails;

public interface WorkflowDetailsRepository extends CrudRepository<WorkflowDetails, Long> {
}