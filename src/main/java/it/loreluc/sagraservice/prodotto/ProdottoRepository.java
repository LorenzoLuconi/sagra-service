package it.loreluc.sagraservice.prodotto;

import it.loreluc.sagraservice.jpa.Menu;
import it.loreluc.sagraservice.jpa.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {

    boolean existsByMenu(Menu menu);
}
