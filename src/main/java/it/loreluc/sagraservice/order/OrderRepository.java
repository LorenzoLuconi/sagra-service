package it.loreluc.sagraservice.order;

import it.loreluc.sagraservice.jpa.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
