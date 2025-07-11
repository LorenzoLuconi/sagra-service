package it.loreluc.sagraservice.course;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.loreluc.sagraservice.course.resource.CourseMapper;
import it.loreluc.sagraservice.course.resource.CourseRequest;
import it.loreluc.sagraservice.course.resource.CourseResource;
import it.loreluc.sagraservice.error.ErrorResource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/courses")
@RequiredArgsConstructor
@Tag(name = "Portate")
public class CourseController {
    private final CourseMapper courseMapper;
    private final CourseService courseService;

    @GetMapping("/{id}")
    @Operation(summary = "Tipologia di portata tramite id")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Portata non trovata")
    public CourseResource courseById(@PathVariable("id") Long id) {
        return courseMapper.toResource(courseService.findById(id));
    }

    @GetMapping
    @Operation(summary = "Ricerca tipologia di portate")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Richiesta non valida")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    public List<CourseResource> coursesSearch(@RequestParam(required = false) @Schema(description = "Ricerca del nome con operatore 'contains'") String name) {
        return courseService.search(name).stream().map(courseMapper::toResource).collect(Collectors.toList());
    }

    @PostMapping
    @Operation(summary = "Crea un tipologie di portata")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Richiesta non valida")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Portata con il medesimo nome già esistente")
    public CourseResource courseCreate(@RequestBody @Valid CourseRequest courseRequest) {
        return courseMapper.toResource(courseService.create(courseRequest.getName()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifica una tipologie di portata")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Richiesta non valida")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Portata non trovata")
    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Portata con il medesimo nome già esistente")
    public CourseResource courseUpdate(@PathVariable("id") Long id, @RequestBody @Valid CourseRequest courseRequest) {
        return courseMapper.toResource(courseService.update(id, courseRequest.getName()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancella una tipologie di portata")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Portata non trovata")
    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Portata referenziata in alcuni prodotti")
    public void courseDelete(@PathVariable("id") Long id) {
        courseService.delete(id);
    }
}
