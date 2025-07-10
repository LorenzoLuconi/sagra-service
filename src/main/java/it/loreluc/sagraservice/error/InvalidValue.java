package it.loreluc.sagraservice.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder @AllArgsConstructor @NoArgsConstructor
public class InvalidValue {
    private String field;
    private Object value;
    private String message;

    @JsonIgnore
    private List<Object> msgArgs;


    public static InvalidValue of(String field, String message) {
        return new InvalidValue(field, null, message, null);
    }

    public static InvalidValue of(String field, String message, Object value) {
        return new InvalidValue(field, value, message, null);
    }
}
