package it.loreluc.sagraservice.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "createError")
public class ErrorResourceNotEnoughQuantity {
    private String message;
    private List<InvalidProduct> invalidProducts;
}
