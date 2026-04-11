package it.loreluc.sagraservice;

import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserManagementTest extends CommonTest {

    @Test
    @DataSet(value = {"users.yml"}, cleanBefore = true)
    public void admin_can_list_and_create_users() throws Exception {
        this.mockMvc.perform(get("/v1/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is("lorenzo")))
                .andExpect(jsonPath("$[1].username", is("test")));

        this.mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "cassa1",
                                  "name": "Cassa Uno",
                                  "role": "cashier",
                                  "password": "password123"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is("cassa1")))
                .andExpect(jsonPath("$.name", is("Cassa Uno")))
                .andExpect(jsonPath("$.role", is("cashier")));
    }

    @Test
    @WithMockUser(username = "test", roles = {"CASHIER"})
    @DataSet(value = {"users.yml"}, cleanBefore = true)
    public void cashier_cannot_manage_users() throws Exception {
        this.mockMvc.perform(get("/v1/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
