package it.loreluc.sagraservice.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.loreluc.sagraservice.order.resource.StatOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/stats")
@Tag(name="Stats")
public class OrderStatsController {
    private final OrderService orderService;

    @GetMapping("/orders")
    @Operation(summary = "Statistiche degli ordini e dei prodotti venduti")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", content = @Content)
    @ApiResponse(responseCode = "401", content = @Content)
    @ApiResponse(responseCode = "403", content = @Content)
    public Map<LocalDate, StatOrder> orderStats(@RequestParam(required = false) LocalDate date){
        return orderService.ordersStats(date);
    }
}
