package it.loreluc.sagraservice.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class InvalidProduct {
    public enum InvalidStatus {
        NOT_ENOUGH_QUANTITY,
        LOCKED
    }
    private Long productId;
    private InvalidStatus status;
}
