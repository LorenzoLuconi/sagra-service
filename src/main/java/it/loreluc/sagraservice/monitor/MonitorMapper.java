package it.loreluc.sagraservice.monitor;

import it.loreluc.sagraservice.jpa.Monitor;
import it.loreluc.sagraservice.jpa.MonitorProduct;
import it.loreluc.sagraservice.monitor.resource.MonitorProductResource;
import it.loreluc.sagraservice.monitor.resource.MonitorProductView;
import it.loreluc.sagraservice.monitor.resource.MonitorResource;
import it.loreluc.sagraservice.monitor.resource.MonitorView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MonitorMapper {

    MonitorResource toResource(Monitor monitor);

    @Mapping(target = "productId", source = "product.id")
    MonitorProductResource toResource(MonitorProduct monitorProduct);

    MonitorView toViewResource(Monitor monitor);

    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "quantity", source = "product.productQuantity.quantity")
    MonitorProductView toViewResource(MonitorProduct monitorProduct);


}
