package it.loreluc.sagraservice.error;


import lombok.Getter;

import java.util.Collections;
import java.util.Set;

public class SagraBadRequestException extends RuntimeException {

    @Getter
    private final Set<InvalidValue> invalidValues;

    public SagraBadRequestException(String message) {
        super(message);
        this.invalidValues = Collections.emptySet();
    }

    public SagraBadRequestException(InvalidValue invalidValue) {
        super("Richiesta non valida");
        this.invalidValues = Set.of(invalidValue);
    }

    public SagraBadRequestException(Set<InvalidValue> invalidValues) {
        super("Richiesta non valida");
        this.invalidValues = Set.copyOf(invalidValues);
    }
}
