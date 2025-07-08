package it.loreluc.sagraservice.order;

import it.loreluc.sagraservice.jpa.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdineRepository extends JpaRepository<Order, Long> {
}
