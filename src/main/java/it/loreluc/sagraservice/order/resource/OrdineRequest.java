package it.loreluc.sagraservice.order.resource;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Data
public class OrdineRequest {

    @Length(max = 128)
    private String nome;

    @Length(max = 255)
    private String note;

    private boolean asporto = false;

    private Integer coperti;

    @NotEmpty
    private Set<OrdineProdottoRequest> prodotti;
}
