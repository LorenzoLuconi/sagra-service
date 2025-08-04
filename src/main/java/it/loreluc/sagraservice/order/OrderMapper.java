package it.loreluc.sagraservice.order;

import it.loreluc.sagraservice.jpa.Order;
import it.loreluc.sagraservice.jpa.OrderProduct;
import it.loreluc.sagraservice.order.resource.OrderProductResponse;
import it.loreluc.sagraservice.order.resource.OrderRequest;
import it.loreluc.sagraservice.order.resource.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "serviceCost", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "lastUpdate", ignore = true)
    Order toEntity(OrderRequest orderRequest);

    @Mapping(target = "username", source = "user.username")
    // @Mapping(target = "products", ignore = true)
    OrderResponse toResponse(Order order);

//    @AfterMapping
//    default void afterMapping(@MappingTarget OrderResponse orderResponse, Order order) {
//        // Li estraiamo ordinati, ma in caso di modifica a volte è necessario riordinarli
//        orderResponse.setProducts(
//            order.getProducts().stream()
//                .sorted(Comparator.comparing(OrderProduct::getIdx))
//                .map(this::toResponse)
//                .toList()
//        );
//    }

    @Mapping(target = "productId", source = "product.id")
    OrderProductResponse toResponse(OrderProduct  orderProduct);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "serviceCost", ignore = true)
    @Mapping(target = "lastUpdate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "products", ignore = true)
    void updateEntity(@MappingTarget Order order, OrderRequest orderRequest);
}
