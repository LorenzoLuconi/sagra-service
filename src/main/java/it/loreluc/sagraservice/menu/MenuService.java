package it.loreluc.sagraservice.menu;

import it.loreluc.sagraservice.error.EntityConflictException;
import it.loreluc.sagraservice.jpa.Menu;
import it.loreluc.sagraservice.menu.resource.MenuMapper;
import it.loreluc.sagraservice.product.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper;

    private final ProductRepository productRepository;

    public Menu findById(Long id) {
        return menuRepository.findById(Objects.requireNonNull(id)).orElseThrow(() -> new EntityNotFoundException("Nessun menu trovato con id: " + id));
    }

    @Transactional(rollbackOn = Throwable.class)
    public Menu create(String nomeMenu) {

        if ( menuRepository.existsByNameContainingIgnoreCase(nomeMenu) ) {
            throw new EntityConflictException(String.format("Menu con il nome '%s' già esistente", nomeMenu));

        }

        final Menu menu = new Menu();
        menu.setName(nomeMenu);

        return menuRepository.save(menu);
    }

    @Transactional(rollbackOn = Throwable.class)
    public void delete(Long id) {
        final Menu menu = findById(id);

        if ( productRepository.existsByMenu(menu) ) {
            throw new EntityConflictException(String.format("Impossibile cancellare il menu '%s' in quanto è referenziato in alcuni prodotti", menu.getName()));
        }

        menuRepository.delete(menu);
    }

    @Transactional(rollbackOn = Throwable.class)
    public Menu update(Long menuId, String nomeMenu) {
        final Menu menu = findById(menuId);

        if ( menuRepository.existsByNameContainingIgnoreCaseAndIdNot(nomeMenu, menuId) ) {
            throw new EntityConflictException(String.format("Menu con il nome '%s' già esistente", nomeMenu));
        }

        menu.setName(nomeMenu);

        return menuRepository.save(menu);
    }



    public List<Menu> search(String nome) {
        if ( nome == null || nome.isEmpty() ) {
            return menuRepository.findAll();
        }

        return menuRepository.findAllByNameContains(nome);
    }


 }
