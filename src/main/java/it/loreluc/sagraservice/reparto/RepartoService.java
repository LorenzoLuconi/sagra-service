package it.loreluc.sagraservice.reparto;

import it.loreluc.sagraservice.jpa.Reparto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RepartoService {
    private final RepartoRepository repartoRepository;

    public Reparto findById(Long id ) {
        return repartoRepository.findById(Objects.requireNonNull(id)).orElseThrow(() -> new EntityNotFoundException("Nessun reparto trovato con id: " + id));
    }

    public List<Reparto> search() {
        return repartoRepository.findAll();
    }
}
