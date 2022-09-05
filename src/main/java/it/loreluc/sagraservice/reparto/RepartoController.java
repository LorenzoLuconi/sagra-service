package it.loreluc.sagraservice.reparto;

import it.loreluc.sagraservice.reparto.resource.RepartoMapper;
import it.loreluc.sagraservice.reparto.resource.RepartoResource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/reparti")
@RequiredArgsConstructor
public class RepartoController {

    private final RepartoMapper mapper;
    private final RepartoService service;

    @GetMapping("/{id}")
    public RepartoResource findOne(@PathVariable("id") Long id) {
        return mapper.toResource(service.findById(id));
    }

    @GetMapping
    public List<RepartoResource> search() {
        return service.search().stream().map(mapper::toResource).collect(Collectors.toList());
    }
}
