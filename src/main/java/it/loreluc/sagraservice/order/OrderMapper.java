package it.loreluc.sagraservice.order;

import it.loreluc.sagraservice.jpa.Order;
import it.loreluc.sagraservice.jpa.OrderProduct;
import it.loreluc.sagraservice.order.resource.OrderProductResponse;
import it.loreluc.sagraservice.order.resource.OrderRequest;
import it.loreluc.sagraservice.order.resource.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "serviceCost", ignore = true)
    @Mapping(target = "orderedProducts", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "lastUpdate", ignore = true)
    Order toEntity(OrderRequest orderRequest);

    OrderResponse toResponse(Order order);

    @Mapping(target = "product", source = "product.id")
    OrderProductResponse toResponse(OrderProduct  orderProduct);
}
