package it.loreluc.sagraservice.course;

import it.loreluc.sagraservice.jpa.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAllByNameContainsIgnoreCase(String nome);

    boolean existsByNameIgnoreCaseAndIdNot(String nome, Long id);
    boolean existsByNameIgnoreCase(String nome);

}
