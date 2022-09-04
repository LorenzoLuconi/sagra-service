package it.loreluc.sagraservice.prodotto;

import it.loreluc.sagraservice.prodotto.resource.ProdottoMapper;
import it.loreluc.sagraservice.prodotto.resource.ProdottoResource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/prodotti")
public class ProdottoController {

    private final ProdottoMapper mapper;
    private final ProdottoService service;

    @GetMapping("/{id}")
    public ProdottoResource findOne(@PathVariable("id") Long id) {
        return mapper.toResource(service.findById(id));
    }

    @GetMapping
    public List<ProdottoResource> search() {
        return service.search().stream().map(mapper::toResource).collect(Collectors.toList());
    }
 }
