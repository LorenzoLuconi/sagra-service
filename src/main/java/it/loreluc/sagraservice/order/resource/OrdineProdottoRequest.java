package it.loreluc.sagraservice.order.resource;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"id"})
public class OrdineProdottoRequest {

    private Long id;

    private Integer quantita;

    private String note;


}
