package it.loreluc.sagraservice.error;

public class EntityConflictException extends RuntimeException {

    public EntityConflictException(String message) {
        super(message);
    }
}
