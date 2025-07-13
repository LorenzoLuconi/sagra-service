package it.loreluc.sagraservice;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderTest extends CommonTest {

    @Test
    @Order(50)
    public void order_read_by_id() throws Exception {
        this.mockMvc.perform(get("/v1/orders/{id}", 1).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.customer", is("Lorenzo Luconi")))
                .andExpect(jsonPath("$.note").doesNotExist())
                .andExpect(jsonPath("$.username", is("lorenzo")))
                .andExpect(jsonPath("$.takeAway", is(false)))
                .andExpect(jsonPath("$.serviceNumber", is(6)))
                .andExpect(jsonPath("$.serviceCost", is(3.0)))
                .andExpect(jsonPath("$.totalAmount", is(34.4)))
                .andExpect(jsonPath("$.created", is("2025-07-10T09:49:00")))
                .andExpect(jsonPath("$.lastUpdate", is("2025-07-10T09:50:01")))
                .andExpect(jsonPath("$.products", hasSize(4)))
                .andExpect(jsonPath("$.products[0].productId", is(2)))
                .andExpect(jsonPath("$.products[0].price", is(0.8)))
                .andExpect(jsonPath("$.products[0].quantity", is(3)))
                .andExpect(jsonPath("$.products[1].productId", is(1)))
                .andExpect(jsonPath("$.products[1].price", is(8.0)))
                .andExpect(jsonPath("$.products[1].quantity", is(3)))
                .andExpect(jsonPath("$.products[2].productId", is(5)))
                .andExpect(jsonPath("$.products[2].price", is(1.5)))
                .andExpect(jsonPath("$.products[2].quantity", is(2)))
                .andExpect(jsonPath("$.products[3].productId", is(6)))
                .andExpect(jsonPath("$.products[3].price", is(2.0)))
                .andExpect(jsonPath("$.products[3].quantity", is(1)))
        ;
    }

    @Order(50)
    @Test
    public void order_read_not_found() throws Exception {
        this.mockMvc.perform(get("/v1/orders/{id}", 1111).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", notNullValue()))
        ;
    }

    @Order(50)
    @Test
    public void order_create() throws Exception {
        final String request = """
                {
                   "customer": " Francesco Cossiga",
                   "takeAway": false,
                   "note": "Allergico" ,
                   "serviceNumber": 3,
                   "username": "lorenzo",
                   "products": [
                     {
                       "productId": 2,
                       "quantity": 5
                     },
                     {
                       "productId": 1,
                       "quantity": 3
                     }
                   ]
                 }
                """;
        this.mockMvc.perform(post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.customer", is("Francesco Cossiga")))
                .andExpect(jsonPath("$.note", is("Allergico")))
                .andExpect(jsonPath("$.username", is("lorenzo")))
                .andExpect(jsonPath("$.takeAway", is(false)))
                .andExpect(jsonPath("$.serviceNumber", is(3)))
                .andExpect(jsonPath("$.serviceCost", is(1.5)))
                .andExpect(jsonPath("$.totalAmount", is(29.5)))
                .andExpect(jsonPath("$.created", notNullValue()))
                .andExpect(jsonPath("$.lastUpdate", notNullValue()))
                .andExpect(jsonPath("$.products", hasSize(2)))
                .andExpect(jsonPath("$.products[0].productId", is(2)))
                .andExpect(jsonPath("$.products[0].price", is(0.8)))
                .andExpect(jsonPath("$.products[0].quantity", is(5)))
                .andExpect(jsonPath("$.products[1].productId", is(1)))
                .andExpect(jsonPath("$.products[1].price", is(8.0)))
                .andExpect(jsonPath("$.products[1].quantity", is(3)))
        ;
    }

    @Order(50)
    @Test
    public void order_create_duplicated_products() throws Exception {
        final String request = """
                {
                   "customer": "Francesco Cossiga",
                   "takeAway": false,
                   "note": "Allergico",
                   "serviceNumber": 3,
                   "username": "lorenzo",
                   "products": [
                     {
                       "productId": 2,
                       "quantity": 5
                     },
                     {
                       "productId": 2,
                       "quantity": 3
                     }
                   ]
                 }
                """;
        this.mockMvc.perform(post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", notNullValue()))
        ;
    }

    @Order(50)
    @Test
    public void order_create_no_service() throws Exception {
        final String request = """
                {
                   "customer": "Francesco Cossiga",
                   "takeAway": false,
                   "note": "Allergico",
                   "serviceNumber": 0,
                   "username": "lorenzo",
                   "products": [
                     {
                       "productId": 2,
                       "quantity": 5
                     },
                     {
                       "productId": 1,
                       "quantity": 3
                     }
                   ]
                 }
                """;
        this.mockMvc.perform(post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.serviceNumber", is(0)))
                .andExpect(jsonPath("$.serviceCost", is(0)))
                .andExpect(jsonPath("$.totalAmount", is(28.0)))
        ;
    }

    @Order(50)
    @Test
    public void order_create_take_away() throws Exception {
        final String request = """
                {
                   "customer": "Francesco Cossiga",
                   "takeAway": true,
                   "serviceNumber": 0,
                   "note": "Allergico",
                   "username": "lorenzo",
                   "products": [
                     {
                       "productId": 2,
                       "quantity": 5
                     },
                     {
                       "productId": 1,
                       "quantity": 3
                     }
                   ]
                 }
                """;
        this.mockMvc.perform(post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.customer", is("Francesco Cossiga")))
                .andExpect(jsonPath("$.note", is("Allergico")))
                .andExpect(jsonPath("$.username", is("lorenzo")))
                .andExpect(jsonPath("$.takeAway", is(true)))
                .andExpect(jsonPath("$.serviceNumber", is(0)))
                .andExpect(jsonPath("$.serviceCost", is(0)))
                .andExpect(jsonPath("$.totalAmount", is(28.0)))
                .andExpect(jsonPath("$.created", notNullValue()))
                .andExpect(jsonPath("$.lastUpdate", notNullValue()))
                .andExpect(jsonPath("$.products", hasSize(2)))
                .andExpect(jsonPath("$.products[0].productId", is(2)))
                .andExpect(jsonPath("$.products[0].price", is(0.8)))
                .andExpect(jsonPath("$.products[0].quantity", is(5)))
                .andExpect(jsonPath("$.products[1].productId", is(1)))
                .andExpect(jsonPath("$.products[1].price", is(8.0)))
                .andExpect(jsonPath("$.products[1].quantity", is(3)))
        ;
    }

    @Order(50)
    @Test
    public void order_create_take_away_with_service() throws Exception {
        final String request = """
                {
                   "customer": "Francesco Cossiga",
                   "takeAway": true,
                   "serviceNumber": 2,
                   "note": "Allergico",
                   "username": "lorenzo",
                   "products": [
                     {
                       "productId": 2,
                       "quantity": 5
                     },
                     {
                       "productId": 1,
                       "quantity": 3
                     }
                   ]
                 }
                """;
        this.mockMvc.perform(post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.invalidValues", hasSize(1)))
                .andExpect(jsonPath("$.invalidValues[0].field", is("serviceNumber")))
                .andExpect(jsonPath("$.invalidValues[0].value", is(2)))
                .andExpect(jsonPath("$.invalidValues[0].message", notNullValue()))
        ;
    }

    @Order(50)
    @Test
    public void order_create_missing_customer() throws Exception {
        final String request = """
                {
                   "takeAway": false,
                   "note": "Allergico",
                   "serviceNumber": 3,
                   "username": "lorenzo",
                   "products": [
                     {
                       "productId": 2,
                       "quantity": 5
                     },
                     {
                       "productId": 2,
                       "quantity": 3
                     }
                   ]
                 }
                """;
        this.mockMvc.perform(post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.invalidValues", hasSize(1)))
                .andExpect(jsonPath("$.invalidValues[0].field", is("customer")))
                .andExpect(jsonPath("$.invalidValues[0].message", notNullValue()))

        ;
    }

    @Order(50)
    @Test
    public void order_create_invalid_product() throws Exception {
        final String request = """
                {
                   "customer": "Francesco Cossiga",
                   "takeAway": false,
                   "note": "Allergico",
                   "serviceNumber": 3,
                   "username": "lorenzo",
                   "products": [
                     {
                       "productId": 2222,
                       "quantity": 5
                     },
                     {
                       "productId": 1,
                       "quantity": 3
                     }
                   ]
                 }
                """;
        this.mockMvc.perform(post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.invalidValues", hasSize(1)))
                .andExpect(jsonPath("$.invalidValues[0].field", is("productId")))
                .andExpect(jsonPath("$.invalidValues[0].value", is(2222)))
                .andExpect(jsonPath("$.invalidValues[0].message", notNullValue()))
        ;
    }

    @Order(50)
    @Test
    public void order_create_invalid_product_quantity() throws Exception {
        final String request = """
                {
                   "customer": "Francesco Cossiga",
                   "takeAway": false,
                   "note": "Allergico",
                   "serviceNumber": 3,
                   "username": "lorenzo",
                   "products": [
                     {
                       "productId": 2,
                       "quantity": 0
                     },
                     {
                       "productId": 1,
                       "quantity": 3
                     }
                   ]
                 }
                """;
        this.mockMvc.perform(post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.invalidValues", hasSize(1)))
                .andExpect(jsonPath("$.invalidValues[0].field", is("products[0].quantity")))
                .andExpect(jsonPath("$.invalidValues[0].value", is(0)))
                .andExpect(jsonPath("$.invalidValues[0].message", notNullValue()))
        ;
    }

    @Order(100)
    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void order_delete() throws Exception {

        checkProductQuantity(2, 500);
        checkProductQuantity(5, 30);

        this.mockMvc.perform(get("/v1/orders/{id}", 2).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.products", hasSize(2)))
                .andExpect(jsonPath("$.products[0].productId", is(2)))
                .andExpect(jsonPath("$.products[0].quantity", is(5)))
                .andExpect(jsonPath("$.products[1].productId", is(5)))
                .andExpect(jsonPath("$.products[1].quantity", is(5)))
        ;

        this.mockMvc.perform(delete("/v1/orders/{id}", 2)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent())
        ;

        this.mockMvc.perform(get("/v1/orders/{id}", 2).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", notNullValue()))
        ;

        checkProductQuantity(2, 505);
        checkProductQuantity(5, 35);
    }

    @Order(50)
    @Test
    public void order_delete_not_found() throws Exception {
        this.mockMvc.perform(delete("/v1/orders/{id}", 2222)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", notNullValue()))
        ;
    }

    @Order(110)
    @Test
    public void order_update_customer() throws Exception {

        // Quantità devono rimanere uguali
        checkProductQuantity(1, 200);
        checkProductQuantity(2, 505);
        checkProductQuantity(5, 35);
        checkProductQuantity(6, 1000);

        final String request = """
                {
                      "customer": "Carlo Azeglio Ciampi",
                      "takeAway": false,
                      "serviceNumber": 6,
                      "username": "lorenzo",
                      "products": [
                          {
                              "productId": 2,
                              "quantity": 3
                          },
                          {
                              "productId": 1,
                              "quantity": 3
                          },
                          {
                              "productId": 5,
                              "quantity": 2
                          },
                          {
                              "productId": 6,
                              "quantity": 1
                          }
                      ]
                  }
                """;
        this.mockMvc.perform(put("/v1/orders/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.customer", is("Carlo Azeglio Ciampi")))
                .andExpect(jsonPath("$.serviceNumber", is(6)))
                .andExpect(jsonPath("$.serviceCost", is(3.0)))
                .andExpect(jsonPath("$.totalAmount", is(34.4)))
        ;

        checkProductQuantity(1, 200);
        checkProductQuantity(2, 505);
        checkProductQuantity(5, 35);
        checkProductQuantity(6, 1000);
    }

    @Order(110)
    @Test
    public void order_update_service() throws Exception {

        checkProductQuantity(1, 200);
        checkProductQuantity(2, 505);
        checkProductQuantity(5, 35);
        checkProductQuantity(6, 1000);

        final String request = """
                {
                      "customer": "Lorenzo Luconi",
                      "takeAway": false,
                      "serviceNumber": 4,
                      "username": "lorenzo",
                      "products": [
                          {
                              "productId": 2,
                              "quantity": 3
                          },
                          {
                              "productId": 1,
                              "quantity": 3
                          },
                          {
                              "productId": 5,
                              "quantity": 2
                          },
                          {
                              "productId": 6,
                              "quantity": 1
                          }
                      ]
                  }
                """;
        this.mockMvc.perform(put("/v1/orders/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.serviceNumber", is(4)))
                .andExpect(jsonPath("$.serviceCost", is(2.0)))
                .andExpect(jsonPath("$.totalAmount", is(33.4)))
        ;

        checkProductQuantity(1, 200);
        checkProductQuantity(2, 505);
        checkProductQuantity(5, 35);
        checkProductQuantity(6, 1000);
    }



    @Order(120)
    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void order_update_remove_product() throws Exception {

        final int quantity1 = checkProductQuantity(1, 200);
        final int quantity2 = checkProductQuantity(6, 1000);
        checkProductQuantity(2, 505);
        checkProductQuantity(5, 35);

        this.mockMvc.perform(get("/v1/orders/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.products" ,hasSize(4)))
        ;

        final String request = """
                {
                      "customer": "Lorenzo Luconi",
                      "takeAway": false,
                      "serviceNumber": 6,
                      "username": "lorenzo",
                      "products": [
                          {
                              "productId": 2,
                              "quantity": 3
                          },
                          {
                              "productId": 5,
                              "quantity": 2
                          }
                      ]
                  }
                """;
        this.mockMvc.perform(put("/v1/orders/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.serviceNumber", is(6)))
                .andExpect(jsonPath("$.serviceCost", is(3.0)))
                .andExpect(jsonPath("$.totalAmount", is(8.4)))
                .andExpect(jsonPath("$.products" ,hasSize(2)))
                .andExpect(jsonPath("$.products[0].productId", is(2)))
                .andExpect(jsonPath("$.products[0].price", is(0.8)))
                .andExpect(jsonPath("$.products[0].quantity", is(3)))
                .andExpect(jsonPath("$.products[1].productId", is(5)))
                .andExpect(jsonPath("$.products[1].price", is(1.5)))
                .andExpect(jsonPath("$.products[1].quantity", is(2)))
        ;

        checkProductQuantity(1, quantity1 + 3);
        checkProductQuantity(6, quantity2 + 1);
        checkProductQuantity(2, 505);
        checkProductQuantity(5, 35);
    }



    @Order(130)
    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void order_update_add_product() throws Exception {

        final int quantity1 = checkProductQuantity(2, 505);
        final int quantity2 = checkProductQuantity(5, 35);

        this.mockMvc.perform(get("/v1/orders/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.products", hasSize(2)))
        ;

        final String request = """
                {
                      "customer": "Lorenzo Luconi",
                      "takeAway": false,
                      "serviceNumber": 6,
                      "username": "lorenzo",
                      "products": [
                          {
                               "productId": 2,
                               "quantity": 3,
                               "price": 0.8
                           },
                           {
                               "productId": 1,
                               "quantity": 3,
                               "price": 8.0
                           },
                           {
                               "productId": 5,
                               "quantity": 2,
                               "price": 1.5
                           },
                           {
                               "productId": 6,
                               "quantity": 1,
                               "price": 2.0
                           }
                      ]
                  }
                """;
        this.mockMvc.perform(put("/v1/orders/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.serviceNumber", is(6)))
                .andExpect(jsonPath("$.serviceCost", is(3.0)))
                .andExpect(jsonPath("$.totalAmount", is(34.4)))
                .andExpect(jsonPath("$.products" ,hasSize(4)))
                .andExpect(jsonPath("$.products[0].productId", is(2)))
                .andExpect(jsonPath("$.products[0].price", is(0.8)))
                .andExpect(jsonPath("$.products[0].quantity", is(3)))
                .andExpect(jsonPath("$.products[1].productId", is(1)))
                .andExpect(jsonPath("$.products[1].price", is(8.0)))
                .andExpect(jsonPath("$.products[1].quantity", is(3)))
                .andExpect(jsonPath("$.products[2].productId", is(5)))
                .andExpect(jsonPath("$.products[2].price", is(1.5)))
                .andExpect(jsonPath("$.products[2].quantity", is(2)))
                .andExpect(jsonPath("$.products[3].productId", is(6)))
                .andExpect(jsonPath("$.products[3].price", is(2.0)))
                .andExpect(jsonPath("$.products[3].quantity", is(1)))
        ;

        checkProductQuantity(2, quantity1 - 3);
        checkProductQuantity(5, quantity2 - 1);
    }

    public void order_update_change_quantity() {

    }


    public void order_update_mixed_update() {

    }

    public void order_update_to_take_away() {

    }
    public void order_update_from_take_away() {

    }

    private int checkProductQuantity(int productId, int expectedQuantity) throws Exception {
        final MvcResult result = this.mockMvc.perform(get("/v1/products/{id}", productId).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productId)))
                .andExpect(jsonPath("$.quantity", is(expectedQuantity)))
                .andReturn();

        return JsonPath.read(result.getResponse().getContentAsString(), "$.quantity");
    }

}
