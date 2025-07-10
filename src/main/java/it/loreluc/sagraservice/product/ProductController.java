package it.loreluc.sagraservice.product;

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

import static it.loreluc.sagraservice.error.InvalidProduct.InvalidStatus.NOT_ENOUGH_QUANTITY;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductMapper productMapper;
    private final ProductService productService;

    @GetMapping("/{productId}")
    public ProductResponse findProductById(@PathVariable Long productId) {
        return productMapper.toResource(productService.findById(productId));
    }

    @GetMapping
    public List<ProductResponse> searchProducts(ProductSearchRequest searchRequest) {
        return productService.search(searchRequest).stream().map(productMapper::toResource).collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody @Valid ProductRequest productRequest) {
        return productMapper.toResource(productService.create(productRequest));
    }

    @PutMapping("/{productId}")
    public ProductResponse updateProduct(@PathVariable Long productId, @RequestBody @Valid ProductRequest productRequest) {
       return productMapper.toResource(productService.update(productId, productRequest));
    }

    @PutMapping("/{productId}/sellLock")
    public ProductResponse sellLockProduct(@PathVariable Long productId) {
        return productMapper.toResource(productService.sellLock(productId, true));
    }

    @PutMapping("/{productId}/sellUnlock")
    public ProductResponse sellUnLockProduct(@PathVariable Long productId) {
        return productMapper.toResource(productService.sellLock(productId, false));
    }

    @PutMapping("/{productId}/updateQuantity")
    public ProductResponse updateProductQuantity(@PathVariable Long productId, @RequestBody @Valid ProductQuantityRequest productQuantityRequest) throws SagraQuantitaNonSufficiente {
        if ( productService.updateProductQuantity(productId, productQuantityRequest.getQuantityVariation())) {
            return productMapper.toResource(productService.findById(productId));
        } else {
            throw new SagraQuantitaNonSufficiente(InvalidProduct.of(productId, NOT_ENOUGH_QUANTITY));
        }
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long productId) {
       productService.delete(productId);
    }


}
