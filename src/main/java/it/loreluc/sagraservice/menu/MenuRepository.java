package it.loreluc.sagraservice.menu;

import it.loreluc.sagraservice.jpa.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
