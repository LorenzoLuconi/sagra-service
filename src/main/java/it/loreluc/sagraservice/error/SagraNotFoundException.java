package it.loreluc.sagraservice.error;

public class SagraNotFoundException extends RuntimeException {
    public SagraNotFoundException(String message) {
        super(message);
    }
}
