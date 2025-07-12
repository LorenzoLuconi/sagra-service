package it.loreluc.sagraservice;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MonitorTest extends CommonTest {

    @Test
    public void monitor_read_by_id() throws Exception {
        this.mockMvc.perform(get("/v1/monitors/{id}", 1).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Cucina")))
                .andExpect(jsonPath("$.products", hasSize(3)))
                .andExpect(jsonPath("$.products[0].productId", is(4)))
                .andExpect(jsonPath("$.products[0].priority", is(1)))
                .andExpect(jsonPath("$.products[1].productId", is(1)))
                .andExpect(jsonPath("$.products[1].priority", is(2)))
                .andExpect(jsonPath("$.products[2].productId", is(3)))
                .andExpect(jsonPath("$.products[2].priority", is(3)))
        ;
    }

    @Test
    public void monitor_read_not_found() throws Exception {
        this.mockMvc.perform(get("/v1/monitors/{id}", 1111).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", notNullValue()))
        ;
    }

    @Test
    public void monitor_create() throws Exception {
        final String request = """
                {
                     "name": "  Griglia  ",
                     "products": [
                         {
                             "productId": 1,
                             "priority": 2
                         },
                         {
                             "productId": 2,
                             "priority": 1
                         },
                         {
                             "productId": 3,
                             "priority": 3
                         }
                     ]
                 }
                """;
        this.mockMvc.perform(post("/v1/monitors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Griglia")))
                .andExpect(jsonPath("$.products", hasSize(3)))
                .andExpect(jsonPath("$.products[0].productId", is(2)))
                .andExpect(jsonPath("$.products[0].priority", is(1)))
                .andExpect(jsonPath("$.products[1].productId", is(1)))
                .andExpect(jsonPath("$.products[1].priority", is(2)))
                .andExpect(jsonPath("$.products[2].productId", is(3)))
                .andExpect(jsonPath("$.products[2].priority", is(3)))
        ;
    }

    @Test
    public void monitor_create_product_not_found() throws Exception {
        final String request = """
                {
                     "name": "Griglia",
                     "products": [
                         {
                             "productId": 1,
                             "priority": 2
                         },
                         {
                             "productId": 2,
                             "priority": 1
                         },
                         {
                             "productId": 3111,
                             "priority": 3
                         }
                     ]
                 }
                """;
        this.mockMvc.perform(post("/v1/monitors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.invalidValues", hasSize(1)))
                .andExpect(jsonPath("$.invalidValues[0].field", is("productId")))
                .andExpect(jsonPath("$.invalidValues[0].value", is(3111)))
                .andExpect(jsonPath("$.invalidValues[0].message", notNullValue()))
        ;
    }

    @Test
    public void monitor_create_bad_request() throws Exception {
        final String request = """
                {
                     "products": [
                         {
                             "productId": 1,
                             "priority": 2
                         },
                         {
                             "productId": 2,
                             "priority": 1
                         },
                         {
                             "productId": 3
                         }
                     ]
                 }
                """;
        this.mockMvc.perform(post("/v1/monitors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.invalidValues", hasSize(2)))
        ;
    }

    @Test
    public void monitor_create_conflict() throws Exception {
        final String request = """
                {
                     "name": "Cucina",
                     "products": [
                         {
                             "productId": 1,
                             "priority": 2
                         },
                         {
                             "productId": 2,
                             "priority": 1
                         },
                         {
                             "productId": 3,
                             "priority": 3
                         }
                     ]
                 }
                """;
        this.mockMvc.perform(post("/v1/monitors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", notNullValue()))
        ;
    }

    @Test
    public void monitor_delete() throws Exception {
        this.mockMvc.perform(delete("/v1/monitors/{id}", 1).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent())
        ;
    }


    @Test
    public void monitor_update() throws Exception {
        final String request = """
                {
                      "name": "Cucina 2",
                      "products": [
                          {
                              "productId": 4,
                              "priority": 1
                          },
                          {
                              "productId": 1,
                              "priority": 3
                          },
                          {
                              "productId": 2,
                              "priority": 2
                          }
                      ]
                  }
                """;
        this.mockMvc.perform(put("/v1/monitors/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Cucina 2")))
                .andExpect(jsonPath("$.products", hasSize(3)))
                .andExpect(jsonPath("$.products[0].productId", is(4)))
                .andExpect(jsonPath("$.products[0].priority", is(1)))
                .andExpect(jsonPath("$.products[1].productId", is(2)))
                .andExpect(jsonPath("$.products[1].priority", is(2)))
                .andExpect(jsonPath("$.products[2].productId", is(1)))
                .andExpect(jsonPath("$.products[2].priority", is(3)))
        ;
    }

    @Test
    public void monitor_update_conflict() throws Exception {
        final String request = """
                     {
                     "name": "Test",
                     "products": [
                         {
                             "productId": 1,
                             "priority": 2
                         },
                         {
                             "productId": 2,
                             "priority": 1
                         },
                         {
                             "productId": 3,
                             "priority": 3
                         }
                     ]
                 }
                """;
        this.mockMvc.perform(put("/v1/monitors/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", notNullValue()))
        ;
    }

    @Test
    public void monitor_search() throws Exception {
        this.mockMvc.perform(get("/v1/monitors")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Cucina")))
                .andExpect(jsonPath("$[1].name", is("Test")))
        ;
    }

    @Test
    public void monitor_view() throws Exception {
        this.mockMvc.perform(get("/v1/monitors/{id}/view", 1).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Cucina")))
                .andExpect(jsonPath("$.lastUpdate", notNullValue()))
                .andExpect(jsonPath("$.products", hasSize(3)))
                .andExpect(jsonPath("$.products[0].name", is("Grigliata Rosticciana")))
                .andExpect(jsonPath("$.products[0].quantity", is(100)))
                .andExpect(jsonPath("$.products[1].name", is("Tordelli")))
                .andExpect(jsonPath("$.products[1].quantity", is(200)))
                .andExpect(jsonPath("$.products[2].name", is("Grigliata Salsicce")))
                .andExpect(jsonPath("$.products[2].quantity", is(75)))
        ;
    }
}
