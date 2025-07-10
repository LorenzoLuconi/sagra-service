package it.loreluc.sagraservice.menu;

import it.loreluc.sagraservice.menu.resource.MenuMapper;
import it.loreluc.sagraservice.menu.resource.MenuRequest;
import it.loreluc.sagraservice.menu.resource.MenuResource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/menu")
@RequiredArgsConstructor
public class MenuController {
    private final MenuMapper menuMapper;
    private final MenuService menuService;

    @GetMapping("/{id}")
    public MenuResource findOne(@PathVariable("id") Long id) {
        return menuMapper.toResource(menuService.findById(id));
    }

    @GetMapping
    public List<MenuResource> search(@RequestParam(required = false) String name) {
        return menuService.search(name).stream().map(menuMapper::toResource).collect(Collectors.toList());
    }

    @PostMapping
    public MenuResource create(@RequestBody @Valid MenuRequest menuRequest) {
        return menuMapper.toResource(menuService.create(menuRequest.getName()));
    }

    @PutMapping("/{id}")
    public MenuResource update(@PathVariable("id") Long id, @RequestBody @Valid MenuRequest menuRequest) {
        return menuMapper.toResource(menuService.update(id, menuRequest.getName()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        menuService.delete(id);
    }
}
