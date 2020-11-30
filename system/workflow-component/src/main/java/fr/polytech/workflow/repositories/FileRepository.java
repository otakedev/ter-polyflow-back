package fr.polytech.workflow.repositories;

import fr.polytech.workflow.models.File;
import org.springframework.data.repository.CrudRepository;

public interface FileRepository extends CrudRepository<File, Long> {
}