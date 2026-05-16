package it.loreluc.sagraservice.appconfiguration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.loreluc.sagraservice.appconfiguration.resource.*;
import it.loreluc.sagraservice.error.ErrorResource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/configurations")
@RequiredArgsConstructor
@Tag(name = "Configurazioni applicazione web")
public class AppConfigurationController {
    private final AppConfigurationService appConfigurationService;
    private final AppConfigurationMapper appConfigurationMapper;

    @GetMapping
    @Operation(summary = "Ottiene tutta la configurazione applicativa")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    public List<AppConfigurationGroupResource> getAll() {
        return appConfigurationMapper.toGroupResources(appConfigurationService.findAll());
    }

    @GetMapping("/{group}")
    @Operation(summary = "Ottiene la configurazione applicativa di un gruppo")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Gruppo non trovato")
    public AppConfigurationGroupResource getGroup(@PathVariable("group") String group) {
        return appConfigurationMapper.toGroupResource(group, appConfigurationService.findByGroup(group));
    }

    @GetMapping("/{group}/{key}")
    @Operation(summary = "Ottiene una singola chiave di configurazione applicativa")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Configurazione non trovata")
    public AppConfigurationValueResource getValue(@PathVariable("group") String group, @PathVariable("key") String key) {
        return appConfigurationMapper.toValueResource(appConfigurationService.findByGroupAndKey(group, key));
    }

    @PutMapping
    @Operation(summary = "Aggiorna tutta la configurazione applicativa")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Richiesta non valida")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Gruppo o configurazione non trovati")
    public List<AppConfigurationGroupResource> updateAll(@RequestBody @Valid AppConfigurationUpdateRequest request) {
        return appConfigurationMapper.toGroupResources(appConfigurationService.updateAll(request));
    }

    @PutMapping("/{group}")
    @Operation(summary = "Aggiorna la configurazione applicativa di un gruppo")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Richiesta non valida")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Gruppo o configurazione non trovati")
    public AppConfigurationGroupResource updateGroup(@PathVariable("group") String group, @RequestBody @Valid AppConfigurationGroupValuesUpdateRequest request) {
        return appConfigurationMapper.toGroupResource(group, appConfigurationService.updateGroup(group, request));
    }

    @PutMapping("/{group}/{key}")
    @Operation(summary = "Aggiorna una singola chiave di configurazione applicativa")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Richiesta non valida")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Configurazione non trovata")
    public AppConfigurationValueResource updateValue(@PathVariable("group") String group, @PathVariable("key") String key, @RequestBody @Valid AppConfigurationSingleValueUpdateRequest request) {
        return appConfigurationMapper.toValueResource(appConfigurationService.updateValue(group, key, request.getValue()));
    }
}
