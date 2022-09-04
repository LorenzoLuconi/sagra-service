package it.loreluc.sagraservice.security;

import it.loreluc.sagraservice.jpa.Utente;
import org.springframework.data.repository.CrudRepository;

public interface UtentiRepository extends CrudRepository<Utente, String> {
}
