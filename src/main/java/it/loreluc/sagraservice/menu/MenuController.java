package it.loreluc.sagraservice.menu;

import it.loreluc.sagraservice.menu.resource.MenuCreateResource;
import it.loreluc.sagraservice.menu.resource.MenuMapper;
import it.loreluc.sagraservice.menu.resource.MenuResource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public List<MenuResource> search(@RequestParam(name = "nome", required = false) String nome) {
        return service.search(nome).stream().map(mapper::toResource).collect(Collectors.toList());
    }

    @PostMapping
    public MenuResource create(@RequestBody @Valid MenuCreateResource menuCreateResource) {
        return mapper.toResource(service.create(menuCreateResource));
    }

    @PutMapping("/{id}")
    public MenuResource update(@PathVariable("id") Long id, @RequestBody @Valid MenuCreateResource menuCreateResource) {
        return mapper.toResource(service.update(id, menuCreateResource));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
