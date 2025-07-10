package it.loreluc.sagraservice.discount.resource;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Data
public class DiscountRequest {

    @NotEmpty
    @Length(max = 32)
    private String name;

    @NotNull
    @Min(1) @Max(100)
    private BigDecimal rate;
}
