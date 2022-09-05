package it.loreluc.sagraservice.menu;

import it.loreluc.sagraservice.menu.resource.MenuMapper;
import it.loreluc.sagraservice.menu.resource.MenuResource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/menu")
@RequiredArgsConstructor
public class MenuController {
    private final MenuMapper mapper;
    private final MenuService service;

    @GetMapping("/{id}")
    public MenuResource findOne(@PathVariable("id") Long id) {
        return mapper.toResource(service.findById(id));
    }

    @GetMapping
    public List<MenuResource> search() {
        return service.search().stream().map(mapper::toResource).collect(Collectors.toList());
    }
}
