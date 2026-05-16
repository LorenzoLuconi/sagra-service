package it.loreluc.sagraservice.appconfiguration.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import it.loreluc.sagraservice.appconfiguration.AppConfigurationType;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "AppConfigurationValue", description = "Valore di configurazione applicativa")
public class AppConfigurationValueResource {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String key;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String value;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private AppConfigurationType type;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> allowedValues;
}
