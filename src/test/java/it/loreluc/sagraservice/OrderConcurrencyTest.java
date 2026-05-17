package it.loreluc.sagraservice;

import com.github.database.rider.core.api.dataset.DataSet;
import com.jayway.jsonpath.JsonPath;
import it.loreluc.sagraservice.order.OrderRepository;
import it.loreluc.sagraservice.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

public class OrderConcurrencyTest extends CommonTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DataSet(value = {"courses.yml", "departments.yml", "products.yml", "users.yml", "app_configurations.yml"}, cleanBefore = true)
    public void parallel_order_creation_keeps_stock_consistent() throws Exception {
        final int initialAvailability = 30;
        final int attempts = 60;
        final ConcurrentLinkedQueue<Integer> statuses = new ConcurrentLinkedQueue<>();

        runInParallel(attempts, idx -> statuses.add(createOrderStatus(5, 1)));

        final long created = statuses.stream().filter(status -> status == 201).count();
        final long rejected = statuses.stream().filter(status -> status == 450).count();

        assertEquals(initialAvailability, created);
        assertEquals(attempts - initialAvailability, rejected);
        assertProductConsistency(5, initialAvailability);
        assertEquals(initialAvailability, orderRepository.findAll().size());
    }

    @Test
    @DataSet(value = {"courses.yml", "departments.yml", "products.yml", "users.yml", "app_configurations.yml"}, cleanBefore = true)
    public void parallel_order_updates_keep_stock_consistent() throws Exception {
        final int initialAvailability = 30;
        final List<Long> orderIds = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            orderIds.add(createOrderId(5, 1, "Update-" + i));
        }

        final ConcurrentLinkedQueue<Integer> statuses = new ConcurrentLinkedQueue<>();
        runInParallel(orderIds.size(), idx -> statuses.add(updateOrderStatus(orderIds.get(idx), 5, 3, "Update-" + idx)));

        final long updated = statuses.stream().filter(status -> status == 200).count();
        final long rejected = statuses.stream().filter(status -> status == 450).count();

        assertEquals(7, updated);
        assertEquals(8, rejected);
        assertProductConsistency(5, initialAvailability);
    }

    private void assertProductConsistency(long productId, int initialAvailability) {
        final int availableQuantity = productRepository.findById(productId)
                .orElseThrow()
                .getProductQuantity()
                .getAvailableQuantity();

        final int orderedQuantity = orderRepository.findAll().stream()
                .flatMap(order -> order.getProducts().stream())
                .filter(orderProduct -> orderProduct.getProductId().equals(productId))
                .mapToInt(orderProduct -> orderProduct.getQuantity())
                .sum();

        assertTrue(availableQuantity >= 0);
        assertEquals(initialAvailability, orderedQuantity + availableQuantity);
    }

    private int createOrderStatus(long productId, int quantity) throws Exception {
        return this.mockMvc.perform(post("/v1/orders")
                        .with(user("lorenzo").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(orderPayload(productId, quantity, "Concurrent")))
                .andReturn()
                .getResponse()
                .getStatus();
    }

    private Long createOrderId(long productId, int quantity, String customer) throws Exception {
        final String response = this.mockMvc.perform(post("/v1/orders")
                        .with(user("lorenzo").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(orderPayload(productId, quantity, customer)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return ((Number) JsonPath.read(response, "$.id")).longValue();
    }

    private int updateOrderStatus(long orderId, long productId, int quantity, String customer) throws Exception {
        return this.mockMvc.perform(put("/v1/orders/{orderId}", orderId)
                        .with(user("lorenzo").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(orderPayload(productId, quantity, customer)))
                .andReturn()
                .getResponse()
                .getStatus();
    }

    private String orderPayload(long productId, int quantity, String customer) {
        return """
                {
                  "customer": "%s",
                  "takeAway": false,
                  "serviceNumber": 0,
                  "products": [
                    {
                      "productId": %d,
                      "quantity": %d
                    }
                  ]
                }
                """.formatted(customer, productId, quantity);
    }

    private void runInParallel(int tasks, IndexedTask task) throws Exception {
        final ExecutorService executorService = Executors.newFixedThreadPool(tasks);
        final CountDownLatch ready = new CountDownLatch(tasks);
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch done = new CountDownLatch(tasks);

        try {
            for (int i = 0; i < tasks; i++) {
                final int idx = i;
                executorService.submit(() -> {
                    ready.countDown();
                    try {
                        start.await(10, TimeUnit.SECONDS);
                        task.run(idx);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        done.countDown();
                    }
                });
            }

            assertTrue(ready.await(10, TimeUnit.SECONDS));
            start.countDown();
            assertTrue(done.await(30, TimeUnit.SECONDS));
        } finally {
            executorService.shutdownNow();
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    @FunctionalInterface
    private interface IndexedTask {
        void run(int idx) throws Exception;
    }
}
