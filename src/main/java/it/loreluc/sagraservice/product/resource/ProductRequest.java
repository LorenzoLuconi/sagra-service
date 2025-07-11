package it.loreluc.sagraservice.product.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {

    @NotEmpty
    private String name;

    private String note;

    @NotNull
    private Long departmentId;

    @NotNull
    private Long courseId;

    @NotNull
    @Min(0)
    private BigDecimal price;

    @Schema(description = "Id prodotto collegato, non può essere modificato")
    private Long parentId;

 //   private boolean bloccaVendita;
}
