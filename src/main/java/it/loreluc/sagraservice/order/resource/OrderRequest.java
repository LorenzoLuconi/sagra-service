package it.loreluc.sagraservice.order.resource;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class OrderRequest {

    @NotEmpty
    @Length(max = 128)
    private String customer;

    @Length(max = 255)
    private String note;

    private boolean takeAway = false;

    @Min(1)
    private Integer serviceNumber;

    @NotEmpty
    private List<@Valid OrderProductRequest> orderedProducts;
}
