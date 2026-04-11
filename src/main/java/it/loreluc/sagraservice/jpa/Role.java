package it.loreluc.sagraservice.jpa;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ruolo utente")
public enum Role {
    @Schema(description = "Amministratore con permessi di gestione utenti")
    admin,
    @Schema(description = "Operatore di cassa")
    cashier
}
