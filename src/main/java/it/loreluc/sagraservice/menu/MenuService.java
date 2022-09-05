package it.loreluc.sagraservice.menu;

import it.loreluc.sagraservice.error.EntityConflictException;
import it.loreluc.sagraservice.error.SagraBadRequestException;
import it.loreluc.sagraservice.jpa.Menu;
import it.loreluc.sagraservice.menu.resource.MenuCreateResource;
import it.loreluc.sagraservice.menu.resource.MenuMapper;
import it.loreluc.sagraservice.prodotto.ProdottoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository repository;
    private final MenuMapper mapper;

    private final ProdottoRepository prodottoRepository;

    public Menu findById(Long id) {
        return repository.findById(Objects.requireNonNull(id)).orElseThrow(() -> new EntityNotFoundException("Messun menu trovato con id: " + id));
    }

    public List<Menu> search(String nome) {
        if ( nome != null ) {
            return repository.findAllByNomeContains(nome);
        }

        return repository.findAll();
    }

    @Transactional
    public Menu create(MenuCreateResource menuCreateResource) {
        if ( repository.existsByNomeContainingIgnoreCase(menuCreateResource.getNome()) ) {
            throw new EntityConflictException("Un altro menu con lo stesso nome già esistente: "+ menuCreateResource.getNome());
        }

        final Menu menu = mapper.map(menuCreateResource);
        return repository.save(menu);
    }

    @Transactional
    public Menu update(Long id, MenuCreateResource menuCreateResource) {
        final Menu menu = findById(id);

        if ( repository.existsByNomeContainingIgnoreCaseAndIdNot(menuCreateResource.getNome(), id) ) {
            throw new EntityConflictException("Un altro menu con lo stesso nome già esistente: "+ menuCreateResource.getNome());
        }

        mapper.update(menu, menuCreateResource);
        return repository.save(menu);
    }

    @Transactional
    public void delete(Long id) {
        final Menu menu = findById(id);

        if ( prodottoRepository.existsByMenu(menu) ) {
            throw new SagraBadRequestException("Sono presenti alcuni prodotti associati al menu: " + menu);
        }

        repository.delete(menu);
    }
 }
