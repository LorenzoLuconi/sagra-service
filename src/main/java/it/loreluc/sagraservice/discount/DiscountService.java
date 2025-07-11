package it.loreluc.sagraservice.discount;

import it.loreluc.sagraservice.discount.resource.DiscountRequest;
import it.loreluc.sagraservice.error.SagraConflictException;
import it.loreluc.sagraservice.error.SagraNotFoundException;
import it.loreluc.sagraservice.jpa.Discount;
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
        return discountRepository.findById(Objects.requireNonNull(id)).orElseThrow(() -> new SagraNotFoundException("Nessun sconto trovato con id: " + id));
    }

    @Transactional(rollbackOn = Throwable.class)
    public Discount create(DiscountRequest discountRequest) {

        if ( discountRepository.existsByNameIgnoreCase(discountRequest.getName()) ) {
            throw new SagraConflictException(String.format("Sconto con il nome '%s' già esistente", discountRequest.getName()));
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

        if ( discountRepository.existsByNameIgnoreCaseAndIdNot(discountRequest.getName(), discountId) ) {
            throw new SagraConflictException(String.format("Sconto con il nome '%s' già esistente", discountRequest.getName()));
        }

        discount.setName(discountRequest.getName());
        discount.setRate(discountRequest.getRate());

        return discountRepository.save(discount);
    }



    public List<Discount> search(String name) {
        if ( name == null || name.isEmpty() ) {
            return discountRepository.findAll();
        }

        return discountRepository.findByNameContainingIgnoreCase(name);
    }
}
