package it.loreluc.sagraservice.monitor;

import it.loreluc.sagraservice.jpa.Monitor;
import it.loreluc.sagraservice.jpa.MonitorProduct;
import it.loreluc.sagraservice.monitor.resource.MonitorProductView;
import it.loreluc.sagraservice.monitor.resource.MonitorResource;
import it.loreluc.sagraservice.monitor.resource.MonitorView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MonitorMapper {

    @Mapping(target = "products", source = "productIds")
    MonitorResource toResource(Monitor monitor);

    MonitorView toViewResource(Monitor monitor);

    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "initialQuantity", source = "product.productQuantity.initialQuantity")
    @Mapping(target = "availableQuantity", source = "product.productQuantity.availableQuantity")
    MonitorProductView toViewResource(MonitorProduct monitorProduct);


}
