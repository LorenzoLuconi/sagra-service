package it.loreluc.sagraservice.discount;

import it.loreluc.sagraservice.jpa.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
    boolean existsByNameIgnoreCase(String name);
    List<Discount> findByNameContainingIgnoreCase(String name);
}
