package it.loreluc.sagraservice.error;

public class SagraAuthorizationException extends RuntimeException {
    public SagraAuthorizationException() {
    }

    public SagraAuthorizationException(String message) {
        super(message);
    }
}
