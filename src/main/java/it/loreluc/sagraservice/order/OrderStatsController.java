package it.loreluc.sagraservice.order;

import it.loreluc.sagraservice.order.resource.OrderStatResult;
import it.loreluc.sagraservice.order.resource.OrderedProductsStats;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/stats")
public class OrderStatsController {
    private final OrderService orderService;

    @GetMapping("/orders")
    public List<OrderStatResult> orderStats(){
        return orderService.ordersStats();
    }

    @GetMapping("/orderedProducts")
    public Map<LocalDate, List<OrderedProductsStats>> orderedProductsStats(){
        return orderService.orderedProductsStats();
    }
}
