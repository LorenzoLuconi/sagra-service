package it.loreluc.sagraservice.appconfiguration.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "AppConfigurationGroup", description = "Gruppo di configurazione applicativa")
public class AppConfigurationGroupResource {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String group;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private List<AppConfigurationValueResource> keys;
}
