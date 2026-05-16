package it.loreluc.sagraservice.appconfiguration.resource;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class AppConfigurationValueUpdateRequest {
    @NotEmpty
    @Length(max = 64)
    private String key;

    private String value;
}
