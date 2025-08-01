package it.loreluc.sagraservice;

import com.github.database.rider.core.api.dataset.DataSet;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderTest extends CommonTest {

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
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
                .andExpect(jsonPath("$.serviceCost", is(0.5)))
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

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
    public void order_read_not_found() throws Exception {
        this.mockMvc.perform(get("/v1/orders/{id}", 1111).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", notNullValue()))
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
    public void order_create() throws Exception {

        checkProductQuantity(2, 500);
        checkProductQuantity(1, 200);

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
        final MvcResult result = this.mockMvc.perform(post("/v1/orders")
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
                .andExpect(jsonPath("$.serviceCost", is(0.5)))
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
                .andReturn();
        ;

        final Integer id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");


        this.mockMvc.perform(get("/v1/orders/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.customer", is("Francesco Cossiga")))
                .andExpect(jsonPath("$.note", is("Allergico")))
                .andExpect(jsonPath("$.username", is("lorenzo")))
                .andExpect(jsonPath("$.takeAway", is(false)))
                .andExpect(jsonPath("$.serviceNumber", is(3)))
                .andExpect(jsonPath("$.serviceCost", is(0.5)))
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

        checkProductQuantity(2, 500 - 5);
        checkProductQuantity(1, 200 - 3);
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml","discounts.yml"}, cleanBefore = true)
    public void order_create_discount() throws Exception {

        checkProductQuantity(2, 500);
        checkProductQuantity(1, 200);

        final String request = """
                {
                   "customer": " Francesco Cossiga",
                   "takeAway": false,
                   "note": "Allergico" ,
                   "serviceNumber": 3,
                   "username": "lorenzo",
                   "discountRate": 100,
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
        final MvcResult result = this.mockMvc.perform(post("/v1/orders")
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
                .andExpect(jsonPath("$.discountRate", is(100)))
                .andExpect(jsonPath("$.takeAway", is(false)))
                .andExpect(jsonPath("$.serviceNumber", is(3)))
                .andExpect(jsonPath("$.serviceCost", is(0.5)))
                .andExpect(jsonPath("$.totalAmount", is(0.0)))
                .andExpect(jsonPath("$.created", notNullValue()))
                .andExpect(jsonPath("$.lastUpdate", notNullValue()))
                .andExpect(jsonPath("$.products", hasSize(2)))
                .andExpect(jsonPath("$.products[0].productId", is(2)))
                .andExpect(jsonPath("$.products[0].price", is(0.8)))
                .andExpect(jsonPath("$.products[0].quantity", is(5)))
                .andExpect(jsonPath("$.products[1].productId", is(1)))
                .andExpect(jsonPath("$.products[1].price", is(8.0)))
                .andExpect(jsonPath("$.products[1].quantity", is(3)))
                .andReturn();
        ;

        final Integer id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");


        this.mockMvc.perform(get("/v1/orders/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.customer", is("Francesco Cossiga")))
                .andExpect(jsonPath("$.note", is("Allergico")))
                .andExpect(jsonPath("$.username", is("lorenzo")))
                .andExpect(jsonPath("$.discountRate", is(100.0)))
                .andExpect(jsonPath("$.takeAway", is(false)))
                .andExpect(jsonPath("$.serviceNumber", is(3)))
                .andExpect(jsonPath("$.serviceCost", is(0.5)))
                .andExpect(jsonPath("$.totalAmount", is(0.0)))
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

        checkProductQuantity(2, 500 - 5);
        checkProductQuantity(1, 200 - 3);
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
    public void order_create_sell_locked() throws Exception {
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
                       "productId": 8,
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
                .andExpect(status().is(450))
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.invalidProducts", hasSize(1)))
                .andExpect(jsonPath("$.invalidProducts[0].productId", is(8)))
                .andExpect(jsonPath("$.invalidProducts[0].error", is("LOCKED")))
                .andExpect(jsonPath("$.invalidProducts[0].message", notNullValue()))
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
    public void order_create_not_enough_quantity() throws Exception {

        checkProductQuantity(2, 500);
        checkProductQuantity(1, 200);

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
                       "quantity": 500
                     },
                     {
                       "productId": 1,
                       "quantity": 201
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
                .andExpect(status().is(450))
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.invalidProducts", hasSize(1)))
                .andExpect(jsonPath("$.invalidProducts[0].productId", is(1)))
                .andExpect(jsonPath("$.invalidProducts[0].error", is("NOT_ENOUGH_QUANTITY")))
                .andExpect(jsonPath("$.invalidProducts[0].message", notNullValue()))
        ;

        checkProductQuantity(2, 500);
        checkProductQuantity(1, 200);
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
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

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
    public void order_create_product_not_found() throws Exception {
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
                       "productId": 2222,
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

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
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
                .andExpect(jsonPath("$.serviceCost", is(0.5)))
                .andExpect(jsonPath("$.totalAmount", is(28.0)))
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
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
                .andExpect(jsonPath("$.serviceCost", is(0.5)))
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

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
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

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
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

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
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

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
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

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
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

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
    public void order_delete_not_found() throws Exception {
        this.mockMvc.perform(delete("/v1/orders/{id}", 2222)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", notNullValue()))
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
    public void order_update_customer() throws Exception {

        // Quantità devono rimanere uguali
        checkProductQuantity(1, 200);
        checkProductQuantity(2, 500);
        checkProductQuantity(5, 30);
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
                .andExpect(jsonPath("$.serviceCost", is(0.5)))
                .andExpect(jsonPath("$.totalAmount", is(34.4)))
        ;

        checkProductQuantity(1, 200);
        checkProductQuantity(2, 500);
        checkProductQuantity(5, 30);
        checkProductQuantity(6, 1000);
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml","discounts.yml"}, cleanBefore = true)
    public void order_update_add_discount() throws Exception {

        // Quantità devono rimanere uguali
        checkProductQuantity(1, 200);
        checkProductQuantity(2, 500);
        checkProductQuantity(5, 30);
        checkProductQuantity(6, 1000);

        this.mockMvc.perform(get("/v1/orders/{id}", 1).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.discountRate").doesNotExist());

        final String request = """
                {
                      "customer": "Lorenzo Luconi",
                      "takeAway": false,
                      "serviceNumber": 6,
                      "discountRate": 30,
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
                .andExpect(jsonPath("$.discountRate", is(30)))
                .andExpect(jsonPath("$.totalAmount", is(24.08)))
        ;

        checkProductQuantity(1, 200);
        checkProductQuantity(2, 500);
        checkProductQuantity(5, 30);
        checkProductQuantity(6, 1000);
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml","discounts.yml"}, cleanBefore = true)
    public void order_update_remove_discount() throws Exception {

        this.mockMvc.perform(get("/v1/orders/{id}", 2).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.discountRate", is(10.0)));


        // Quantità devono rimanere uguali
        checkProductQuantity(1, 200);
        checkProductQuantity(2, 500);
        checkProductQuantity(5, 30);
        checkProductQuantity(6, 1000);

        final String request = """
                {
                       "customer": "Sandro Pertini",
                       "note": "test",
                       "takeAway": true,
                       "serviceNumber": 0,
                       "products": [
                            {
                               "productId": 2,
                               "quantity": 5
                           },
                            {
                               "productId": 5,
                               "quantity": 5
                           }
                       ]
                   }
                """;
        this.mockMvc.perform(put("/v1/orders/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.discountRate").doesNotExist())
                .andExpect(jsonPath("$.totalAmount", is(11.5)))
        ;

        checkProductQuantity(1, 200);
        checkProductQuantity(2, 500);
        checkProductQuantity(5, 30);
        checkProductQuantity(6, 1000);
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
    public void order_update_service() throws Exception {

        checkProductQuantity(1, 200);
        checkProductQuantity(2, 500);
        checkProductQuantity(5, 30);
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
                .andExpect(jsonPath("$.serviceCost", is(0.5)))
                .andExpect(jsonPath("$.totalAmount", is(33.4)))
        ;

        checkProductQuantity(1, 200);
        checkProductQuantity(2, 500);
        checkProductQuantity(5, 30);
        checkProductQuantity(6, 1000);
    }



    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
    public void order_update_remove_product() throws Exception {

        final int quantity1 = checkProductQuantity(1, 200);
        final int quantity2 = checkProductQuantity(6, 1000);
        checkProductQuantity(2, 500);
        checkProductQuantity(5, 30);

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
                .andExpect(jsonPath("$.serviceCost", is(0.5)))
                .andExpect(jsonPath("$.totalAmount", is(8.4)))
                .andExpect(jsonPath("$.products" ,hasSize(2)))
                .andExpect(jsonPath("$.products[0].productId", is(2)))
                .andExpect(jsonPath("$.products[0].price", is(0.8)))
                .andExpect(jsonPath("$.products[0].quantity", is(3)))
                .andExpect(jsonPath("$.products[1].productId", is(5)))
                .andExpect(jsonPath("$.products[1].price", is(1.5)))
                .andExpect(jsonPath("$.products[1].quantity", is(2)))
        ;

        this.mockMvc.perform(get("/v1/orders/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.serviceNumber", is(6)))
                .andExpect(jsonPath("$.serviceCost", is(0.5)))
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
        checkProductQuantity(2, 500);
        checkProductQuantity(5, 30);
    }



    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
    public void order_update_change_quantity() throws Exception {

        final int quantity1 = checkProductQuantity(2, 500);
        final int quantity2 = checkProductQuantity(6, 1000);

        this.mockMvc.perform(get("/v1/orders/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.products", hasSize(4)))
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
                               "quantity": 2,
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
                               "quantity": 2,
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
                .andExpect(jsonPath("$.serviceCost", is(0.5)))
                .andExpect(jsonPath("$.totalAmount", is(35.6)))
                .andExpect(jsonPath("$.products" ,hasSize(4)))
                .andExpect(jsonPath("$.products[0].productId", is(2)))
                .andExpect(jsonPath("$.products[0].price", is(0.8)))
                .andExpect(jsonPath("$.products[0].quantity", is(2)))
                .andExpect(jsonPath("$.products[1].productId", is(1)))
                .andExpect(jsonPath("$.products[1].price", is(8.0)))
                .andExpect(jsonPath("$.products[1].quantity", is(3)))
                .andExpect(jsonPath("$.products[2].productId", is(5)))
                .andExpect(jsonPath("$.products[2].price", is(1.5)))
                .andExpect(jsonPath("$.products[2].quantity", is(2)))
                .andExpect(jsonPath("$.products[3].productId", is(6)))
                .andExpect(jsonPath("$.products[3].price", is(2.0)))
                .andExpect(jsonPath("$.products[3].quantity", is(2)))
        ;

        this.mockMvc.perform(get("/v1/orders/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.serviceNumber", is(6)))
                .andExpect(jsonPath("$.serviceCost", is(0.5)))
                .andExpect(jsonPath("$.totalAmount", is(35.6)))
                .andExpect(jsonPath("$.products" ,hasSize(4)))
                .andExpect(jsonPath("$.products[0].productId", is(2)))
                .andExpect(jsonPath("$.products[0].price", is(0.8)))
                .andExpect(jsonPath("$.products[0].quantity", is(2)))
                .andExpect(jsonPath("$.products[1].productId", is(1)))
                .andExpect(jsonPath("$.products[1].price", is(8.0)))
                .andExpect(jsonPath("$.products[1].quantity", is(3)))
                .andExpect(jsonPath("$.products[2].productId", is(5)))
                .andExpect(jsonPath("$.products[2].price", is(1.5)))
                .andExpect(jsonPath("$.products[2].quantity", is(2)))
                .andExpect(jsonPath("$.products[3].productId", is(6)))
                .andExpect(jsonPath("$.products[3].price", is(2.0)))
                .andExpect(jsonPath("$.products[3].quantity", is(2)))
        ;

        checkProductQuantity(2, quantity1 + 1);
        checkProductQuantity(6, quantity2 - 1);
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
    public void order_update_add_products() throws Exception {

        final int quantity1 = checkProductQuantity(3, 75);
        final int quantity2 = checkProductQuantity(4, 100);

        this.mockMvc.perform(get("/v1/orders/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.products", hasSize(4)))
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
                            "productId": 3,
                            "quantity": 2
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
                        },
                        
                        {
                            "productId": 4,
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
                .andExpect(jsonPath("$.serviceNumber", is(6)))
                .andExpect(jsonPath("$.serviceCost", is(0.5)))
                .andExpect(jsonPath("$.totalAmount", is(49.9)))
                .andExpect(jsonPath("$.products" ,hasSize(6)))
                .andExpect(jsonPath("$.products[0].productId", is(2)))
                .andExpect(jsonPath("$.products[0].price", is(0.8)))
                .andExpect(jsonPath("$.products[0].quantity", is(3)))

                .andExpect(jsonPath("$.products[1].productId", is(3)))
                .andExpect(jsonPath("$.products[1].price", is(4.5)))
                .andExpect(jsonPath("$.products[1].quantity", is(2)))

                .andExpect(jsonPath("$.products[2].productId", is(1)))
                .andExpect(jsonPath("$.products[2].price", is(8.0)))
                .andExpect(jsonPath("$.products[2].quantity", is(3)))
                .andExpect(jsonPath("$.products[3].productId", is(5)))
                .andExpect(jsonPath("$.products[3].price", is(1.5)))
                .andExpect(jsonPath("$.products[3].quantity", is(2)))
                .andExpect(jsonPath("$.products[4].productId", is(6)))
                .andExpect(jsonPath("$.products[4].price", is(2.0)))
                .andExpect(jsonPath("$.products[4].quantity", is(1)))

                .andExpect(jsonPath("$.products[5].productId", is(4)))
                .andExpect(jsonPath("$.products[5].price", is(6.5)))
                .andExpect(jsonPath("$.products[5].quantity", is(1)))
        ;

        this.mockMvc.perform(get("/v1/orders/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.serviceNumber", is(6)))
                .andExpect(jsonPath("$.serviceCost", is(0.5)))
                .andExpect(jsonPath("$.totalAmount", is(49.9)))
                .andExpect(jsonPath("$.products" ,hasSize(6)))
                .andExpect(jsonPath("$.products[0].productId", is(2)))
                .andExpect(jsonPath("$.products[0].price", is(0.8)))
                .andExpect(jsonPath("$.products[0].quantity", is(3)))

                .andExpect(jsonPath("$.products[1].productId", is(3)))
                .andExpect(jsonPath("$.products[1].price", is(4.5)))
                .andExpect(jsonPath("$.products[1].quantity", is(2)))

                .andExpect(jsonPath("$.products[2].productId", is(1)))
                .andExpect(jsonPath("$.products[2].price", is(8.0)))
                .andExpect(jsonPath("$.products[2].quantity", is(3)))
                .andExpect(jsonPath("$.products[3].productId", is(5)))
                .andExpect(jsonPath("$.products[3].price", is(1.5)))
                .andExpect(jsonPath("$.products[3].quantity", is(2)))
                .andExpect(jsonPath("$.products[4].productId", is(6)))
                .andExpect(jsonPath("$.products[4].price", is(2.0)))
                .andExpect(jsonPath("$.products[4].quantity", is(1)))

                .andExpect(jsonPath("$.products[5].productId", is(4)))
                .andExpect(jsonPath("$.products[5].price", is(6.5)))
                .andExpect(jsonPath("$.products[5].quantity", is(1)))
        ;

        checkProductQuantity(3, quantity1 - 2);
        checkProductQuantity(4, quantity2 - 1);
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
    public void order_update_switch_products() throws Exception {

        checkProductQuantity(2, 500);
        checkProductQuantity(5, 30);

        this.mockMvc.perform(get("/v1/orders/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.products", hasSize(2)))
                .andExpect(jsonPath("$.products[0].productId", is(2)))
                .andExpect(jsonPath("$.products[1].productId", is(5)))
        ;

        final String request = """
                {
                       "customer": "Sandro Pertini",
                       "note": "test",
                       "takeAway": true,
                       "serviceNumber": 0,
                       "products": [
                            {
                               "productId": 5,
                               "quantity": 5
                           },
                           {
                               "productId": 2,
                               "quantity": 5
                           }
                       ]
                   }
                """;
        this.mockMvc.perform(put("/v1/orders/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.totalAmount", is(11.5)))
                .andExpect(jsonPath("$.products" ,hasSize(2)))

                .andExpect(jsonPath("$.products[0].productId", is(2)))
                .andExpect(jsonPath("$.products[1].productId", is(5)))
        ;

        // ATTENZIONE l'inversione non risulta in risposta alla modifica,
        // questo perché andrebbero riordinati prima di riportarli (si veda OrderMapper) in quanto non estratti dal DB
        // E' l'unico caso, ma d'altronde non ha molto senso spostare i prodotti ordinati
        // Una nuova query dovrebbe portarli nell'ordine modificato
        this.mockMvc.perform(get("/v1/orders/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.products", hasSize(2)))
                .andExpect(jsonPath("$.products[0].productId", is(5)))
                .andExpect(jsonPath("$.products[1].productId", is(2)))
        ;

        checkProductQuantity(2, 500);
        checkProductQuantity(5, 30);
    }


    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
    public void order_update_sell_locked() throws Exception {

        // Quantità devono rimanere uguali
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
                          },
                          {
                              "productId": 8,
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
                .andExpect(status().is(450))
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.invalidProducts", hasSize(1)))
                .andExpect(jsonPath("$.invalidProducts[0].productId", is(8)))
                .andExpect(jsonPath("$.invalidProducts[0].error", is("LOCKED")))
                .andExpect(jsonPath("$.invalidProducts[0].message", notNullValue()))
        ;
    }



    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
    public void order_update_to_takeway() throws Exception {

        // Quantità devono rimanere uguali
        checkProductQuantity(1, 200);
        checkProductQuantity(2, 500);
        checkProductQuantity(5, 30);
        checkProductQuantity(6, 1000);

        final String request = """
                {
                      "customer": "Lorenzo Luconi",
                      "takeAway": true,
                      "serviceNumber": 0,
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
                .andExpect(jsonPath("$.customer", is("Lorenzo Luconi")))
                .andExpect(jsonPath("$.serviceNumber", is(0)))
                .andExpect(jsonPath("$.serviceCost", is(0.5)))
                .andExpect(jsonPath("$.totalAmount", is(31.4)))
        ;

        checkProductQuantity(1, 200);
        checkProductQuantity(2, 500);
        checkProductQuantity(5, 30);
        checkProductQuantity(6, 1000);
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
    public void order_update_to_takeaway_with_service() throws Exception {

        final String request = """
                {
                      "customer": "Lorenzo Luconi",
                      "takeAway": true,
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.invalidValues", hasSize(1)))
                .andExpect(jsonPath("$.invalidValues[0].field", is("serviceNumber")))
                .andExpect(jsonPath("$.invalidValues[0].value", is(6)))
                .andExpect(jsonPath("$.invalidValues[0].message", notNullValue()))
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
    public void order_update_from_takeway() throws Exception {

        checkProductQuantity(2, 500);
        checkProductQuantity(5, 30);

        this.mockMvc.perform(get("/v1/orders/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.products", hasSize(2)))
                .andExpect(jsonPath("$.products[0].productId", is(2)))
                .andExpect(jsonPath("$.products[1].productId", is(5)))
        ;

        final String request = """
                {
                       "customer": "Sandro Pertini",
                       "note": "test",
                       "takeAway": false,
                       "serviceNumber": 2,
                       "products": [
                            {
                               "productId": 2,
                               "quantity": 5
                            },
                            {
                               "productId": 5,
                               "quantity": 5
                            }
                       ]
                   }
                """;
        this.mockMvc.perform(put("/v1/orders/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.totalAmount", is(12.5)))
                .andExpect(jsonPath("$.products" ,hasSize(2)))
        ;

        checkProductQuantity(2, 500);
        checkProductQuantity(5, 30);
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
    public void order_update_not_enough_quantity() throws Exception {

        checkProductQuantity(2, 500);
        checkProductQuantity(5, 30);

        this.mockMvc.perform(get("/v1/orders/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.products", hasSize(2)))
                .andExpect(jsonPath("$.products[0].productId", is(2)))
                .andExpect(jsonPath("$.products[1].productId", is(5)))
        ;

        // productId = 5, erano 5 ordinati, disponibili ancora 30, quindi a 36 deve dare errore
        final String request = """
                {
                       "customer": "Sandro Pertini",
                       "note": "test",
                       "takeAway": false,
                       "serviceNumber": 2,
                       "products": [
                            {
                               "productId": 2,
                               "quantity": 505
                            },
                            {
                               "productId": 5,
                               "quantity": 36
                            }
                       ]
                   }
                """;
        this.mockMvc.perform(put("/v1/orders/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(450))
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.invalidProducts", hasSize(1)))
                .andExpect(jsonPath("$.invalidProducts[0].productId", is(5)))
                .andExpect(jsonPath("$.invalidProducts[0].error", is("NOT_ENOUGH_QUANTITY")))
                .andExpect(jsonPath("$.invalidProducts[0].message", notNullValue()))
        ;

        checkProductQuantity(2, 500);
        checkProductQuantity(5, 30);
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
    public void order_update_duplicated_products() throws Exception {

        checkProductQuantity(2, 500);
        checkProductQuantity(5, 30);

        this.mockMvc.perform(get("/v1/orders/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.products", hasSize(2)))
                .andExpect(jsonPath("$.products[0].productId", is(2)))
                .andExpect(jsonPath("$.products[1].productId", is(5)))
        ;

        final String request = """
                {
                       "customer": "Sandro Pertini",
                       "note": "test",
                       "takeAway": false,
                       "serviceNumber": 2,
                       "products": [
                            {
                               "productId": 2,
                               "quantity": 5
                            },
                            {
                               "productId": 5,
                               "quantity": 5
                            },
                            {
                               "productId": 5,
                               "quantity": 5
                            }
                       ]
                   }
                """;
        this.mockMvc.perform(put("/v1/orders/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", notNullValue()))
        ;

        checkProductQuantity(2, 500);
        checkProductQuantity(5, 30);
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
    public void order_update_mixed() throws Exception {

        // Quantità devono rimanere uguali
        checkProductQuantity(1, 200);
        checkProductQuantity(2, 500);
        checkProductQuantity(5, 30);
        checkProductQuantity(6, 1000);
        checkProductQuantity(9, 1000);
        checkProductQuantity(7, 200); // linked

        // modificato:
        // - name
        // - serviceNumber 6->7
        // - Rimosso prodotto 5
        // - Aggiunto prodotto 7 al posto del 5
        // - Aggiunto prodotto 9 in fondo
        // - Aumentata quantita prodotto 2
        // - Diminuita quantita prodotto 1
        // - aggiunte note
        final String request = """
                {
                      "customer": "Carlo Azeglio Ciampi",
                      "takeAway": false,
                      "note": "test",
                      "serviceNumber": 7,
                      "username": "lorenzo",
                      "products": [
                          {
                              "productId": 2,
                              "quantity": 4
                          },
                          {
                              "productId": 1,
                              "quantity": 2
                          },
                          {
                              "productId": 7,
                              "quantity": 2
                          },
                          {
                              "productId": 6,
                              "quantity": 1
                          },
                          {
                              "productId": 9,
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
                .andExpect(jsonPath("$.customer", is("Carlo Azeglio Ciampi")))
                .andExpect(jsonPath("$.serviceNumber", is(7)))
                .andExpect(jsonPath("$.serviceCost", is(0.5)))
                .andExpect(jsonPath("$.totalAmount", is(30.7)))
                .andExpect(jsonPath("$.products", hasSize(5)))
        ;

        // Rileggiamo perché il ritorno non rispetta l'ordine
        this.mockMvc.perform(get("/v1/orders/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.customer", is("Carlo Azeglio Ciampi")))
                .andExpect(jsonPath("$.serviceNumber", is(7)))
                .andExpect(jsonPath("$.serviceCost", is(0.5)))
                .andExpect(jsonPath("$.totalAmount", is(30.7)))
                .andExpect(jsonPath("$.products", hasSize(5)))
                .andExpect(jsonPath("$.products[0].productId", is(2)))
                .andExpect(jsonPath("$.products[0].quantity", is(4)))
                .andExpect(jsonPath("$.products[1].productId", is(1)))
                .andExpect(jsonPath("$.products[1].quantity", is(2)))
                .andExpect(jsonPath("$.products[2].productId", is(7)))
                .andExpect(jsonPath("$.products[2].quantity", is(2)))
                .andExpect(jsonPath("$.products[3].productId", is(6)))
                .andExpect(jsonPath("$.products[3].quantity", is(1)))
                .andExpect(jsonPath("$.products[4].productId", is(9)))
                .andExpect(jsonPath("$.products[4].quantity", is(2)))
        ;


        checkProductQuantity(1, 200 + 1 - 2); // Linked 7
        checkProductQuantity(2, 500 - 1);
        checkProductQuantity(5, 32);
        checkProductQuantity(7, 200 + 1 - 2);
        checkProductQuantity(6, 1000);
        checkProductQuantity(9, 1000 - 2 );
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
    public void order_search_by_customers() throws Exception {
        this.mockMvc.perform(get("/v1/orders?customer=lore").accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].customer", is("Lorenzo Luconi")))
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
    public void order_search_by_username() throws Exception {
        this.mockMvc.perform(get("/v1/orders?username=test").accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(2)))
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
    public void order_search_by_created() throws Exception {
        this.mockMvc.perform(get("/v1/orders?created=2025-07-11").accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(2)))
        ;
    }

    private int checkProductQuantity(int productId, int expectedQuantity) throws Exception {
        final MvcResult result = this.mockMvc.perform(get("/v1/products/{id}", productId).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productId)))
                .andExpect(jsonPath("$.availableQuantity", is(expectedQuantity)))
                .andReturn();

        return JsonPath.read(result.getResponse().getContentAsString(), "$.availableQuantity");
    }

}
