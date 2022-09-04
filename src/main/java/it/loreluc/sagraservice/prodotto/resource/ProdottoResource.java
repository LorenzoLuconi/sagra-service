package it.loreluc.sagraservice.prodotto.resource;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProdottoResource {

    private Long id;
    private String nome;
    private String note;
    private String reparto;
    private String menu;
    private BigDecimal prezzo;
    // private boolean bloccaVendita;
    // private LocalDateTime created;
    // private LocalDateTime lastupdate;
}
