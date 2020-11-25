package fr.polytech.workflow.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import fr.polytech.workflow.models.Workflow;

public interface WorkflowRepository extends CrudRepository<Workflow, Long> {

    List<Workflow> findAll(Pageable pageable);

    Optional<Workflow> findById(Long id);

    @Query(value = "SELECT * FROM workflow WHERE ?1 LIKE %?2%", nativeQuery = true)
    public List<Workflow> search(String field, String value, Pageable pageable);

    @Query(value = "SELECT * FROM WORKFLOW w WHERE ?1 LIKE %?2%", nativeQuery = true)
    public List<Workflow> search(String field, String value);
}