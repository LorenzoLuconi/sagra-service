package it.loreluc.sagraservice.discount;

import it.loreluc.sagraservice.discount.resource.DiscountResponse;
import it.loreluc.sagraservice.jpa.Discount;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DiscountMapper {
    DiscountResponse toResource(Discount discount);
}
