package it.loreluc.sagraservice.appconfiguration.resource;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class AppConfigurationGroupUpdateRequest {
    @NotEmpty
    @Length(max = 64)
    private String group;

    @NotEmpty
    @Valid
    private List<AppConfigurationValueUpdateRequest> keys;
}
