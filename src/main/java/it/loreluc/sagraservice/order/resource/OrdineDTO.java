package it.loreluc.sagraservice.order.resource;

import it.loreluc.sagraservice.jpa.OrderProduct;
import it.loreluc.sagraservice.jpa.User;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrdineDTO {
    private Long id;
    private String nome;
    private String note;
    private boolean asporto;
    private Integer coperti;
    private BigDecimal costoCoperti;
    private LocalDateTime created;
    private LocalDateTime lastupdate;
    private User userId;

    // FIXME che cosa usiamo
    private List<OrderProduct> prodottiOrdinati = new ArrayList<>();
}
