package fr.polytech.workflow.repositories;

import org.springframework.data.repository.CrudRepository;

import fr.polytech.workflow.models.WorkflowDetails;

public interface WorkflowDetailsRepository extends CrudRepository<WorkflowDetails, Long> {
}