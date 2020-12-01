package fr.polytech.entities.repositories;

import org.springframework.data.repository.CrudRepository;

import fr.polytech.entities.models.WorkflowStep;

public interface WorkflowStepRepository extends CrudRepository<WorkflowStep, Long> {
}