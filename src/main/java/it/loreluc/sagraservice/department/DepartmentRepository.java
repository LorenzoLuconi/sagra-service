package it.loreluc.sagraservice.department;

import it.loreluc.sagraservice.jpa.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByNameContainingIgnoreCase(String nome);
    boolean existsByNameContainingIgnoreCaseAndIdNot(String nome, Long id);

    List<Department> findByNameContaining(String nome);
}
