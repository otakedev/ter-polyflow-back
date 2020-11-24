package fr.polytech.workflow.repositories;

import org.springframework.data.repository.CrudRepository;

import fr.polytech.workflow.models.WorkflowStep;

public interface WorkflowStepRepository extends CrudRepository<WorkflowStep, Long> {
}