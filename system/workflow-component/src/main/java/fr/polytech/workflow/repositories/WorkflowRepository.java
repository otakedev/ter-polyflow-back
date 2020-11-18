package fr.polytech.workflow.repositories;

import org.springframework.data.repository.CrudRepository;

import fr.polytech.workflow.models.Workflow;

public interface WorkflowRepository extends CrudRepository<Workflow, Long> {
}