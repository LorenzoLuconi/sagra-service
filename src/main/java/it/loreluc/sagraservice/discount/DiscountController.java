package it.loreluc.sagraservice.discount;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.loreluc.sagraservice.discount.resource.DiscountRequest;
import it.loreluc.sagraservice.discount.resource.DiscountResponse;
import it.loreluc.sagraservice.error.ErrorResource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/discounts")
@Tag(name = "Sconti")
public class DiscountController {
    private final DiscountMapper discountMapper;
    private final DiscountService discountService;

    @GetMapping("/{discountId}")
    @Operation(summary = "Tipi di sconti tramite id")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Sconto non trovato")
    public DiscountResponse discountById(@PathVariable("discountId") Long id) {
        return discountMapper.toResource(discountService.findById(id));
    }

    @GetMapping
    @Operation(summary = "Ricerca tipi di sconto")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Richiesta non valida")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    public List<DiscountResponse> discountsSearch(@RequestParam(required = false) @Schema(description = "Ricerca del nome con operatore 'contains'") String name) {
        return discountService.search(name).stream().map(discountMapper::toResource).collect(Collectors.toList());
    }

    @PostMapping
    @Operation(summary = "Crea un tipo di sconto")
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Richiesta non valida")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Sconto con il medesimo nome già esistente")
    public DiscountResponse discountCreate(@RequestBody @Valid DiscountRequest discountRequest) {
        return discountMapper.toResource(discountService.create(discountRequest));
    }

    @PutMapping("/{discountId}")
    @Operation(summary = "Modifica una tipologia di sconto")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Richiesta non valida")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Sconto non trovato")
    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Sconto con il medesimo nome già esistente")
    public DiscountResponse updateDiscount(@PathVariable("discountId") Long id, @RequestBody @Valid DiscountRequest discountRequest) {
        return discountMapper.toResource(discountService.update(id, discountRequest));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancella una tipologie di portata")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Sconto non trovato")
    public void discountDelete(@PathVariable("id") Long id) {
        discountService.delete(id);
    }
}
