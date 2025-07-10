package it.loreluc.sagraservice.order;

import it.loreluc.sagraservice.order.resource.OrderRequest;
import it.loreluc.sagraservice.order.resource.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderMapper orderMapper;
    private final OrderService orderService;

    @GetMapping("/{orderId}")
    public OrderResponse getOrderById(@PathVariable Long orderId) {
        return orderMapper.toResponse(orderService.getOrderById(orderId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@RequestBody OrderRequest orderRequest) {
        return orderMapper.toResponse(orderService.createOrder(orderRequest));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<OrderResponse> searchOrders(SearchOrderParams searchOrderParams) {
        return null;
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrderById(@PathVariable Long orderId) {

    }
}
