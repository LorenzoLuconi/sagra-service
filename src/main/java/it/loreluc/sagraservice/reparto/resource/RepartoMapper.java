package it.loreluc.sagraservice.reparto.resource;

import it.loreluc.sagraservice.jpa.Reparto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RepartoMapper {
    RepartoResource toResource(Reparto reparto);
}
