package it.loreluc.sagraservice.menu;

import it.loreluc.sagraservice.jpa.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAllByNomeContains(String nome);

    boolean existsByNomeContainingIgnoreCaseAndIdNot(String nome, Long id);
    boolean existsByNomeContainingIgnoreCase(String nome);

}
