package it.loreluc.sagraservice.monitor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.loreluc.sagraservice.error.ErrorResource;
import it.loreluc.sagraservice.monitor.resource.MonitorResource;
import it.loreluc.sagraservice.monitor.resource.MonitorView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/monitors")
@RequiredArgsConstructor
@Tag(name = "Monitors")
public class MonitorController {
    private final MonitorService monitorService;
    private final MonitorMapper monitorMapper;

    @GetMapping("/{monitorId}")
    @Operation(summary = "Monitor tramite id")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Monitor non trovato")
    public MonitorResource monitorById(@PathVariable("monitorId") Long id) {
        return monitorMapper.toResource(monitorService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crea un monitor")
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Richiesta non valida")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Sconto con il medesimo nome già esistente")
    public MonitorResource   monitorCreate(@RequestBody @Valid MonitorResource monitorResource) {
        return monitorMapper.toResource(monitorService.create(monitorResource));
    }

    @PutMapping("/{monitorId}")
    @Operation(summary = "Modifica un monitor")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Richiesta non valida")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Monitor non trovato")
    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Monitor con il medesimo nome già esistente")
    public MonitorResource updateMonitor(@PathVariable("monitorId") Long id, @RequestBody @Valid MonitorResource monitorResource) {
        return monitorMapper.toResource(monitorService.update(id, monitorResource));
    }

    @DeleteMapping("/{monitorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancella un monitor")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Sconto non trovato")
    public void monitorDelete(@PathVariable("monitorId") Long id) {
        monitorService.delete(id);
    }

    @GetMapping
    @Operation(summary = "Ricerca dei monitor")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    public List<MonitorResource> monitorSearch() {
        return monitorService.search().stream().map(monitorMapper::toResource).collect(Collectors.toList());
    }

    @GetMapping("/{monitorId}/view")
    @Operation(summary = "Dati per visualizzazione monitor")
    @ApiResponse(responseCode = "200")
    public MonitorView monitorView(@PathVariable Long monitorId) {
        return monitorMapper.toViewResource(monitorService.findById(monitorId));
    }
}
