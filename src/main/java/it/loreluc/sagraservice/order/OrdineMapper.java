package it.loreluc.sagraservice.order;

import it.loreluc.sagraservice.jpa.Order;
import it.loreluc.sagraservice.order.resource.OrdineRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrdineMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    Order mapToOrdine(OrdineRequest ordineRequest);
}
