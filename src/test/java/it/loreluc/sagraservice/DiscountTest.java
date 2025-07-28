package it.loreluc.sagraservice;

import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DiscountTest extends CommonTest {

    @Test
    @DataSet( value = "discounts.yml", cleanBefore = true)
    public void discount_read_by_id() throws Exception {
        this.mockMvc.perform(get("/v1/discounts/{id}", 1).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Sconto 20% amici")))
                .andExpect(jsonPath("$.rate", is(20.0)))
        ;
    }

    @Test
    @DataSet( value = "discounts.yml", cleanBefore = true)
    public void discount_read_not_found() throws Exception {
        this.mockMvc.perform(get("/v1/discounts/{id}", 1111).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", notNullValue()))
        ;
    }

    @Test
    @DataSet( value = "discounts.yml", cleanBefore = true)
    public void discount_create() throws Exception {
        final String request = """
                {
                    "name": "Nuovo Sconto 30%",
                    "rate": 30
                }
                """;
        this.mockMvc.perform(post("/v1/discounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Nuovo Sconto 30%")))
                .andExpect(jsonPath("$.rate", is(30)))
        ;
    }

    @Test
    @DataSet( value = "discounts.yml", cleanBefore = true)
    public void discount_create_wrong_rate() throws Exception {
        final String request = """
                {
                    "name": "Nuovo Sconto 30%",
                    "rate": 101
                }
                """;
        this.mockMvc.perform(post("/v1/discounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.invalidValues", hasSize(1)))
                .andExpect(jsonPath("$.invalidValues[0].field", is("rate")))
                .andExpect(jsonPath("$.invalidValues[0].value", is(101)))
                .andExpect(jsonPath("$.invalidValues[0].message", notNullValue()))
        ;
    }

    @Test
    @DataSet( value = "discounts.yml", cleanBefore = true)
    public void discount_create_conflict() throws Exception {
        final String request = """
                {
                    "name": "Sconto 20% amici",
                    "rate": 25
                }
                """;
        this.mockMvc.perform(post("/v1/discounts")
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
    @DataSet( value = "discounts.yml", cleanBefore = true)
    public void discount_delete() throws Exception {
        this.mockMvc.perform(delete("/v1/discounts/{id}", 2).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent())
        ;
    }

    @Test
    @DataSet( value = "discounts.yml", cleanBefore = true)
    public void discount_update() throws Exception {
        final String request = """
                {
                    "name": "Sconto 25% amici",
                    "rate": 25
                }
                """;
        this.mockMvc.perform(put("/v1/discounts/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Sconto 25% amici")))
                .andExpect(jsonPath("$.rate", is(25)))
        ;
    }

    @Test
    @DataSet( value = "discounts.yml", cleanBefore = true)
    public void discount_update_conflict() throws Exception {
        final String request = """
                {
                      "name": "Sconto 20% amici",
                      "rate": 25
                }
                """;
        this.mockMvc.perform(put("/v1/discounts/{id}", 2)
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
    @DataSet( value = "discounts.yml", cleanBefore = true)
    public void discount_search() throws Exception {
        this.mockMvc.perform(get("/v1/discounts")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Sconto 20% amici")))
                .andExpect(jsonPath("$[1].name", is("Sconto 100% amici speciali")))
        ;
    }

    @Test
    @DataSet( value = "discounts.yml", cleanBefore = true)
    public void discount_search_by_name() throws Exception {
        this.mockMvc.perform(get("/v1/discounts?name=speciali")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Sconto 100% amici speciali")))
        ;
    }
}
