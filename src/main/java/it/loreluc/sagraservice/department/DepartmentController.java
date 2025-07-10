package it.loreluc.sagraservice.department;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.loreluc.sagraservice.department.resource.DepartmentMapper;
import it.loreluc.sagraservice.department.resource.DepartmentRequest;
import it.loreluc.sagraservice.department.resource.DepartmentResource;
import it.loreluc.sagraservice.error.ErrorResource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/departments")
@RequiredArgsConstructor
@Tag(name = "Reparti")
public class DepartmentController {

    private final DepartmentMapper departmentMapper;
    private final DepartmentService departmentService;

    @GetMapping("/{departmentId}")
    @Operation(summary = "Reparto tramite id")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Reparto trovata")
    public DepartmentResource getDepartmentById(@PathVariable("departmentId") Long id) {
        return departmentMapper.toResource(departmentService.findById(id));
    }

    @PutMapping("/{departmentId}")
    @Operation(summary = "Modifica un reparto")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Richiesta non valida")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Reparto non trovata")
    public DepartmentResource updateDepartment(@PathVariable("departmentId") Long id, @RequestBody @Valid DepartmentRequest departmentRequest) {
        return departmentMapper.toResource(departmentService.update(id, departmentRequest.getName()));
    }

    @GetMapping
    @Operation(summary = "Ricerca reparti")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Richiesta non valida")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    public List<DepartmentResource> searchDepartments(@RequestParam(required = false) @Schema(description = "Ricerca del nome con operatore 'contains'") String name) {
        return departmentService.search(name).stream().map(departmentMapper::toResource).collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crea un reparto")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Richiesta non valida")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    public DepartmentResource createDepartment(@RequestBody @Valid DepartmentRequest departmentRequest) {
        return departmentMapper.toResource(departmentService.create(departmentRequest.getName()));
    }

    @DeleteMapping("/{departmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancella un reparto")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Reparto non trovata")
    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Reparto referenziato in alcuni prodotti")
    public void deleteDepartment(@PathVariable Long departmentId) {
        departmentService.delete(departmentId);
    }
}
