package it.loreluc.sagraservice.menu.resource;

import it.loreluc.sagraservice.jpa.Menu;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuMapper {

    MenuResource toResource(Menu menu);
}
