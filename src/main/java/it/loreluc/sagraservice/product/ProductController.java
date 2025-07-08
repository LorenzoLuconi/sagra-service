package it.loreluc.sagraservice.product;

import it.loreluc.sagraservice.product.resource.ProductMapper;
import it.loreluc.sagraservice.product.resource.ProductRequest;
import it.loreluc.sagraservice.product.resource.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long productId) {
        // TODO
    }


}
