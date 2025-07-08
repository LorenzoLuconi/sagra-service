package it.loreluc.sagraservice.order.resource;

import com.fasterxml.jackson.annotation.JsonFormat;
import it.loreluc.sagraservice.jpa.OrderProduct;
import it.loreluc.sagraservice.jpa.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrdineResponse {

    private Long id;
    private String nome;
    private String note;
    private boolean asporto;
    private Integer coperti;
    private BigDecimal costoCoperti;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastupdate;
    private User userId;

    // FIXME che cosa usiamo
    private List<OrderProduct> prodottiOrdinati = new ArrayList<>();
}
