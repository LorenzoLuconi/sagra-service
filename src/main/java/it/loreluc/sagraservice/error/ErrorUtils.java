package it.loreluc.sagraservice.error;

import jakarta.validation.ConstraintViolation;

import java.util.function.Function;

public interface ErrorUtils {
    Function<ConstraintViolation<?>, InvalidValue> CONSTRAINT_TO_WRONGVALUE = v -> {
        String message;
        try {
            final String violation = v.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
            message = switch (violation) {
                case "NotNull", "NotEmpty", "NotBlank" -> "Valore obbligatorio";
                default -> v.getMessage();
            };
        } catch (RuntimeException e) {
            message = "Valore non valido";
        }

        return InvalidValue.builder()
                .message(message)
                .field(v.getPropertyPath().toString())
                .value(v.getInvalidValue()).build();
    };
}
