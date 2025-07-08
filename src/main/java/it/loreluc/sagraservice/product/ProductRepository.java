package it.loreluc.sagraservice.product;

import it.loreluc.sagraservice.jpa.Department;
import it.loreluc.sagraservice.jpa.Menu;
import it.loreluc.sagraservice.jpa.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByNameIgnoreCase(String name);
    boolean existsByMenu(Menu menu);
    boolean existsByDepartment(Department department);
}
