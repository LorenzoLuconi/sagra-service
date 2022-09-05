package it.loreluc.sagraservice.reparto;

import it.loreluc.sagraservice.jpa.Reparto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepartoRepository extends JpaRepository<Reparto, Long> {
}
