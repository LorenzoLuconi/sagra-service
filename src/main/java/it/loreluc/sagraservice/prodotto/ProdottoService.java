package it.loreluc.sagraservice.prodotto;

import it.loreluc.sagraservice.jpa.Prodotto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdottoService {

    private final ProdottoRepository prodottoRepository;

    public Prodotto findById(Long id) {
        return prodottoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Prodotto non trovato con id: " + id));
    }

    public List<Prodotto> search() {
        return prodottoRepository.findAll();
    }
}
