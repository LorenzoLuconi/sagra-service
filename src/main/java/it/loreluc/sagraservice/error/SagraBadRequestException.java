package it.loreluc.sagraservice.error;


public class SagraBadRequestException extends RuntimeException {
    // TODO rivedere, il messaggio e basta è poco utile
    public SagraBadRequestException(String message) {
        super(message);
    }
}
