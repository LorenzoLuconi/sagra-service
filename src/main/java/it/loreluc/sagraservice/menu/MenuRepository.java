package it.loreluc.sagraservice.menu;

import it.loreluc.sagraservice.jpa.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAllByNameContains(String nome);

    boolean existsByNameContainingIgnoreCaseAndIdNot(String nome, Long id);
    boolean existsByNameContainingIgnoreCase(String nome);

}
