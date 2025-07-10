package it.loreluc.sagraservice.discount;

import it.loreluc.sagraservice.jpa.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    boolean existsByNameIgnoreCase(String name);
    List<Discount> findByNameIgnoreCase(String name);
}
