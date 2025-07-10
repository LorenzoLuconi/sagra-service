package it.loreluc.sagraservice.discount;

import it.loreluc.sagraservice.discount.resource.DiscountRequest;
import it.loreluc.sagraservice.error.EntityConflictException;
import it.loreluc.sagraservice.jpa.Discount;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DiscountService {
    private final DiscountRepository discountRepository;

    public Discount findById(Long id) {
        return discountRepository.findById(Objects.requireNonNull(id)).orElseThrow(() -> new EntityNotFoundException("Nessun sconto trovato con id: " + id));
    }

    @Transactional(rollbackOn = Throwable.class)
    public Discount create(DiscountRequest discountRequest) {

        if ( discountRepository.existsByNameIgnoreCase(discountRequest.getName()) ) {
            throw new EntityConflictException(String.format("Sconto con il nome '%s' già esistente", discountRequest));

        }

        final Discount discount = new Discount();
        discount.setName(discountRequest.getName());
        discount.setRate(discountRequest.getRate());

        return discountRepository.save(discount);
    }

    @Transactional(rollbackOn = Throwable.class)
    public void delete(Long id) {
        discountRepository.delete(findById(id));
    }

    @Transactional(rollbackOn = Throwable.class)
    public Discount update(Long discountId, DiscountRequest discountRequest) {
        final Discount discount = findById(discountId);

        discount.setName(discountRequest.getName());
        discount.setRate(discountRequest.getRate());

        return discountRepository.save(discount);
    }



    public List<Discount> search(String nome) {
        if ( nome == null || nome.isEmpty() ) {
            return discountRepository.findAll();
        }

        return discountRepository.findByNameIgnoreCase(nome);
    }
}
