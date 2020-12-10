package fr.polytech.entities.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fr.polytech.entities.models.Minor;
import fr.polytech.entities.models.MinorCourse;

public interface MinorCourseRepository extends CrudRepository<MinorCourse, Long> {

	MinorCourse getCourseByCode(String code);

	List<MinorCourse> findCourseByMinor(Minor minor);
}
