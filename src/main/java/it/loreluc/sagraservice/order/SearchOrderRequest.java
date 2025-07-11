package it.loreluc.sagraservice.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.core.annotations.ParameterObject;

import java.time.LocalDate;

@Data
@ParameterObject
public class SearchOrderRequest {
    @Schema(description = "Ricerca con operatore 'contains'")
    private String customer;

    private String username;

    private LocalDate created;
}
