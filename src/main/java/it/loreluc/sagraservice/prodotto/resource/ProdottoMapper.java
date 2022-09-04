package it.loreluc.sagraservice.prodotto.resource;

import it.loreluc.sagraservice.jpa.Prodotto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProdottoMapper {
    @Mapping(target = "menu", source = "menu.nome")
    @Mapping(target = "reparto", source = "reparto.nome")
    ProdottoResource toResource(Prodotto prodotto);
}
