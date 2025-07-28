package it.loreluc.sagraservice.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data @NoArgsConstructor
@AllArgsConstructor(staticName = "createError")
public class ErrorResource {

    @Schema(requiredMode =  Schema.RequiredMode.REQUIRED)
    private String message;
    private Set<InvalidValue> invalidValues;

    public static ErrorResource createError(String message) {
        final ErrorResource errorResource = new ErrorResource();
        errorResource.message = message;
        errorResource.invalidValues = null;

        return errorResource;
    }
}
