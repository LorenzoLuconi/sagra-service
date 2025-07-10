package it.loreluc.sagraservice.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.loreluc.sagraservice.error.ErrorResource;
import it.loreluc.sagraservice.error.InvalidProduct;
import it.loreluc.sagraservice.error.SagraQuantitaNonSufficiente;
import it.loreluc.sagraservice.product.resource.ProductMapper;
import it.loreluc.sagraservice.product.resource.ProductQuantityRequest;
import it.loreluc.sagraservice.product.resource.ProductRequest;
import it.loreluc.sagraservice.product.resource.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static it.loreluc.sagraservice.error.InvalidProduct.ProductError.NOT_ENOUGH_QUANTITY;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/products")
@Tag(name="Prodotti")
public class ProductController {

    private final ProductMapper productMapper;
    private final ProductService productService;

    @GetMapping("/{productId}")
    @Operation(summary = "Prodotto tramite id")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Richiesta non valida")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    public ProductResponse productById(@PathVariable Long productId) {
        return productMapper.toResource(productService.findById(productId));
    }

    @GetMapping
    @Operation(summary = "Ricerca prodotti")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Richiesta non valida")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    public List<ProductResponse> productsSearch(ProductSearchRequest searchRequest) {
        return productService.search(searchRequest).stream().map(productMapper::toResource).collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crea un reparto")
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Richiesta non valida")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Prodotto con medesimo nome già esistente")
    public ProductResponse productCreate(@RequestBody @Valid ProductRequest productRequest) {
        return productMapper.toResource(productService.create(productRequest));
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Modifica un reparto")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Richiesta non valida")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Prodotto non trovata")
    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Prodotto con il medesimo nome già esistente")
    public ProductResponse productUpdate(@PathVariable Long productId, @RequestBody @Valid ProductRequest productRequest) {
       return productMapper.toResource(productService.update(productId, productRequest));
    }

    @PutMapping("/{productId}/sellLock")
    @Operation(summary = "Blocca la vendita di un prodotto")
    @ApiResponse(responseCode = "20")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Prodotto non trovata")
    public ProductResponse productSellLock(@PathVariable Long productId) {
        return productMapper.toResource(productService.sellLock(productId, true));
    }

    @PutMapping("/{productId}/sellUnlock")
    @Operation(summary = "Sblocca la vendita di un prodotto")
    @ApiResponse(responseCode = "20")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Prodotto non trovata")
    public ProductResponse productSellUnlock(@PathVariable Long productId) {
        return productMapper.toResource(productService.sellLock(productId, false));
    }

    @PutMapping("/{productId}/updateQuantity")
    @Operation(summary = "Modifica la quantità disponibile di un prodotto")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Richiesta non valida")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Prodotto non trovata")
    @ApiResponse(responseCode = "450", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Quantità prodotto insufficiente per la variazione richiesta")
    public ProductResponse productUpdateQuantity(@PathVariable Long productId, @RequestBody @Valid ProductQuantityRequest productQuantityRequest) throws SagraQuantitaNonSufficiente {
        if ( productService.updateProductQuantity(productId, productQuantityRequest.getQuantityVariation())) {
            return productMapper.toResource(productService.findById(productId));
        } else {
            throw new SagraQuantitaNonSufficiente(InvalidProduct.of(productId, NOT_ENOUGH_QUANTITY));
        }
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancella un prodotto")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Prodotto non trovata")
    @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Prpdotto referenziato in alcuni ordini")
    public void productDelete(@PathVariable Long productId) {
       productService.delete(productId);
    }


}
