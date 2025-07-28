package it.loreluc.sagraservice.order.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"productId"})
@Schema(name = "OrderedProductRequest")
public class OrderProductRequest {

    @NotNull
    private Long productId;

    @NotNull @Min(1)
    private Integer quantity;

    private String note;
}
