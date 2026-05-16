package it.loreluc.sagraservice.appconfiguration.resource;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AppConfigurationUpdateRequest {
    @NotEmpty
    @Valid
    private List<AppConfigurationGroupUpdateRequest> groups;
}
