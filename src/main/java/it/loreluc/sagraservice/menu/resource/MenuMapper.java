package it.loreluc.sagraservice.menu.resource;

import it.loreluc.sagraservice.jpa.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MenuMapper {

    MenuResource toResource(Menu menu);

    @Mapping(target = "id", ignore = true)
    Menu map(MenuCreateResource menuCreateResource);

    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget Menu menu, MenuCreateResource menuCreateResource);
}
