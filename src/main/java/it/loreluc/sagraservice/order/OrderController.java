package it.loreluc.sagraservice.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.loreluc.sagraservice.error.ErrorResource;
import it.loreluc.sagraservice.order.resource.CountResponse;
import it.loreluc.sagraservice.order.resource.OrderRequest;
import it.loreluc.sagraservice.order.resource.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
@Tag(name="Ordini")
public class OrderController {

    private final OrderMapper orderMapper;
    private final OrderService orderService;

    @GetMapping("/{orderId}")
    @Operation(summary = "Ordine tramite id")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Ordine non trovato")
    public OrderResponse orderById(@PathVariable Long orderId) {
        return orderMapper.toResponse(orderService.getOrderById(orderId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crea un ordine")
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "450", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Alcuni prodotti sono bloccati alla vendita o quantità insufficiente")
    public OrderResponse orderCreate(@RequestBody @Valid OrderRequest orderRequest) {
        return orderMapper.toResponse(orderService.createOrder(orderRequest));
    }

    @GetMapping
    @Operation(summary = "Ricerca degli ordini")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Richiesta non valida")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    public List<OrderResponse> ordersSearch(SearchOrderRequest searchOrderRequest, @ParameterObject Pageable pageable) {
        return orderService.searchOrders(searchOrderRequest, pageable).stream().map(orderMapper::toResponse).toList();
    }

    @GetMapping("/count")
    @Operation(summary = "Contare gli ordini")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Richiesta non valida")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    public CountResponse ordersCount(SearchOrderRequest searchOrderRequest) {
        return new CountResponse(orderService.countOrders(searchOrderRequest));
    }

    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancella un ordine")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    public void orderDelete(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
    }

    @PutMapping("/{orderId}")
    @Operation(summary = "Crea un ordine")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Ordine non trovato")
    @ApiResponse(responseCode = "450", content = @Content(schema = @Schema(implementation = ErrorResource.class)), description = "Alcuni prodotti sono bloccati alla vendita o quantità insufficiente")
    public OrderResponse orderUpdate(@PathVariable Long orderId, @RequestBody @Valid OrderRequest orderRequest) {
        return orderMapper.toResponse(orderService.updateOrder(orderId, orderRequest));
    }
}
