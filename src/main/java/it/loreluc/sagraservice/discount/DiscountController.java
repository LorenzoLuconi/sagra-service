package it.loreluc.sagraservice.discount;

import it.loreluc.sagraservice.discount.resource.DiscountRequest;
import it.loreluc.sagraservice.discount.resource.DiscountResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/discounts")
public class DiscountController {
    private final DiscountMapper discountMapper;
    private final DiscountService discountService;

    @GetMapping("/{discountId}")
    public DiscountResponse findOne(@PathVariable("discountId") Long id) {
        return discountMapper.toResource(discountService.findById(id));
    }

    @GetMapping
    public List<DiscountResponse> search(@RequestParam(required = false) String name) {
        return discountService.search(name).stream().map(discountMapper::toResource).collect(Collectors.toList());
    }

    @PostMapping
    public DiscountResponse create(@RequestBody @Valid DiscountRequest discountRequest) {
        return discountMapper.toResource(discountService.create(discountRequest));
    }

    @PutMapping("/{discountId}")
    public DiscountResponse update(@PathVariable("discountId") Long id, @RequestBody @Valid DiscountRequest discountRequest) {
        return discountMapper.toResource(discountService.update(id, discountRequest));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        discountService.delete(id);
    }
}
