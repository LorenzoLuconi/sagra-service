package it.loreluc.sagraservice.order.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(name = "OrderedProductRequest")
public class OrderRequest {

    @NotEmpty
    @Length(max = 128)
    private String customer;

    @Length(max = 255)
    private String note;

    private boolean takeAway = false;

    @Min(1)
    private Integer serviceNumber;

    @Min(1) @Max(100)
    private BigDecimal discountRate;

    @NotEmpty
    private List<@Valid OrderProductRequest> orderedProducts;
}
