package it.loreluc.sagraservice.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "createError")
public class ErrorResourceNotEnoughQuantity {
    @Schema(requiredMode =  Schema.RequiredMode.REQUIRED)
    private String message;

    @Schema(requiredMode =  Schema.RequiredMode.REQUIRED)
    private List<InvalidProduct> invalidProducts;
}
