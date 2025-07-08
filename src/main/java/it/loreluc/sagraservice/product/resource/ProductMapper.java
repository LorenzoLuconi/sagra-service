package it.loreluc.sagraservice.product.resource;

import it.loreluc.sagraservice.department.DepartmentService;
import it.loreluc.sagraservice.jpa.Product;
import it.loreluc.sagraservice.jpa.ProductQuantity;
import it.loreluc.sagraservice.menu.MenuService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {MenuService.class, DepartmentService.class})
public interface ProductMapper {
    @Mapping(target = "quantity", source = "productQuantity.quantity")
    @Mapping(target = "menu", source = "menu.id")
    @Mapping(target = "department", source = "department.id")
    ProductResponse toResource(Product product);

    @Mapping(target = "sellLocked", ignore = true)
    @Mapping(target = "productQuantity", ignore = true)
    @Mapping(target = "lastUpdate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    Product toEntity(ProductRequest productRequest);

    @AfterMapping
    default void afterCreate(@MappingTarget Product product, ProductRequest productRequest) {
        final ProductQuantity productQuantity = new ProductQuantity();
        productQuantity.setProduct(product);
        productQuantity.setQuantity(0);

        product.setProductQuantity(productQuantity);
    }
}
