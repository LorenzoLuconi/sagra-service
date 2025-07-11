package it.loreluc.sagraservice.order.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@Schema(name = "OrderRequest")
public class OrderRequest {

    @NotEmpty
    @Length(max = 128)
    private String customer;

    @Length(max = 255)
    private String note;

    private boolean takeAway = false;

    @Min(1)
    private Integer serviceNumber;

    private Long discountId;

    @NotEmpty
    private List<@Valid OrderProductRequest> products;
}
