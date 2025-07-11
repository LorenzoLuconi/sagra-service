package it.loreluc.sagraservice.monitor;

import it.loreluc.sagraservice.error.InvalidValue;
import it.loreluc.sagraservice.error.SagraBadRequestException;
import it.loreluc.sagraservice.error.SagraConflictException;
import it.loreluc.sagraservice.error.SagraNotFoundException;
import it.loreluc.sagraservice.jpa.Monitor;
import it.loreluc.sagraservice.jpa.MonitorProduct;
import it.loreluc.sagraservice.jpa.MonitorProductId;
import it.loreluc.sagraservice.jpa.Product;
import it.loreluc.sagraservice.monitor.resource.MonitorProductResource;
import it.loreluc.sagraservice.monitor.resource.MonitorResource;
import it.loreluc.sagraservice.product.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class MonitorService {
    private final MonitorRepository monitorRepository;
    private final ProductService productService;

    public Monitor findById(Long monitorId) {
        return monitorRepository.findById(Objects.requireNonNull(monitorId))
                .orElseThrow(() -> new SagraNotFoundException("Nessun monitor trovato con id " + monitorId));
    }

    @Transactional(rollbackOn = Throwable.class)
    public Monitor create(MonitorResource monitorResource) {

        if ( monitorRepository.existsByNameIgnoreCase(monitorResource.getName()) ) {
            throw new SagraConflictException(String.format("Monitor con il nome '%s' già esistente", monitorResource.getName()));
        }

        final Monitor monitor = new Monitor();
        monitor.setName(monitorResource.getName());
        setMonitorProducts(monitor, monitorResource.getProducts());

        return monitorRepository.save(monitor);
    }

    @Transactional(rollbackOn = Throwable.class)
    public Monitor update(Long monitorId, MonitorResource monitorResource) {
        final Monitor monitor = findById(monitorId);

        if ( monitorRepository.existsByNameIgnoreCaseAndIdNot(monitorResource.getName(), monitorId) ) {
            throw new SagraConflictException(String.format("Monitor con il nome '%s' già esistente", monitorResource.getName()));
        }

        monitor.setName(monitorResource.getName());
        monitor.getProducts().clear();
        setMonitorProducts(monitor, monitorResource.getProducts());

        return monitorRepository.save(monitor);
    }

    @Transactional(rollbackOn = Throwable.class)
    public void delete(Long monitorId) {
        final Monitor monitor = findById(monitorId);
        monitorRepository.delete(monitor);
    }

    public List<Monitor> search() {
        return monitorRepository.findAll();
    }

    private void setMonitorProducts(Monitor monitor, List<MonitorProductResource> monitorProductResources) {
        monitorProductResources.stream()
                .sorted(Comparator.comparing(MonitorProductResource::getPriority))
                .forEach(mp -> {
                    final Product product;

                    try {
                        product = productService.findById(mp.getProductId());
                    } catch (SagraNotFoundException e) {
                        throw new SagraBadRequestException(
                                InvalidValue.builder().field("productId").value(mp.getProductId()).message("Prodotto non trovato").build()
                        );
                    }
                    final MonitorProduct monitorProduct = new MonitorProduct();
                    monitorProduct.setProduct(product);
                    monitorProduct.setMonitor(monitor);
                    monitorProduct.setPriority(mp.getPriority());
                    monitorProduct.setId(new MonitorProductId(monitor.getId(), product.getId()));
                    monitor.getProducts().add(monitorProduct);
                });
    }
}
