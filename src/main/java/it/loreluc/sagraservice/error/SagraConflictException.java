package it.loreluc.sagraservice.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Set;

@RequiredArgsConstructor
public class SagraConflictException extends RuntimeException {

    @Getter
    private final Set<InvalidValue> invalidValues;

    public SagraConflictException(String message) {
        super(message);
        this.invalidValues = Collections.emptySet();
    }

    public SagraConflictException(String message, InvalidValue invalidValue) {
        super(message);
        this.invalidValues = Set.of(invalidValue);
    }

    public SagraConflictException(String message, Set<InvalidValue> invalidValues) {
        super(message);
        this.invalidValues = invalidValues;
    }
}
