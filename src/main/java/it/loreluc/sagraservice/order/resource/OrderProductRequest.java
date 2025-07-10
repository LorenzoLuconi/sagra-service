package it.loreluc.sagraservice.order.resource;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"id"})
public class OrderProductRequest {

    @NotNull
    private Long productId;

    @NotNull @Min(1)
    private Integer quantity;

    private String note;
}
