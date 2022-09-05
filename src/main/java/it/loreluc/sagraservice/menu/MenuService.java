package it.loreluc.sagraservice.menu;

import it.loreluc.sagraservice.jpa.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    public Menu findById(Long id) {
        return menuRepository.findById(Objects.requireNonNull(id)).orElseThrow(() -> new EntityNotFoundException("Messun menu trovato con id: " + id));
    }

    public List<Menu> search() {
        return menuRepository.findAll();
    }
 }
