package fr.polytech.entities.repositories;

import fr.polytech.entities.models.File;
import org.springframework.data.repository.CrudRepository;

public interface FileRepository extends CrudRepository<File, Long> {
}