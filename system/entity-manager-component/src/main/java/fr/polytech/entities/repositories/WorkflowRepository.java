package fr.polytech.entities.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.domain.Specification;

import fr.polytech.entities.models.Workflow;

public interface WorkflowRepository extends CrudRepository<Workflow, Long>, JpaSpecificationExecutor<Workflow> {

    List<Workflow> findAll(Pageable pageable);

    Page<Workflow> findAll(Specification<Workflow> specification, Pageable pageable);

    Optional<Workflow> findById(Long id);
}