package it.loreluc.sagraservice;

import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthTest extends CommonTest {

    @Test
    @WithAnonymousUser
    @DataSet(value = {"users.yml"}, cleanBefore = true)
    public void login_success() throws Exception {
        this.mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "lorenzo",
                                  "password": "lorenzo"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("lorenzo")))
                .andExpect(jsonPath("$.name", is("Lorenzo")))
                .andExpect(jsonPath("$.role", is("admin")));
    }

    @Test
    @WithAnonymousUser
    @DataSet(value = {"users.yml"}, cleanBefore = true)
    public void login_invalid_password() throws Exception {
        this.mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "lorenzo",
                                  "password": "sbagliata"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", notNullValue()));
    }

    @Test
    @DataSet(value = {"users.yml"}, cleanBefore = true)
    public void me_returns_authenticated_user() throws Exception {
        this.mockMvc.perform(get("/v1/auth/me").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("lorenzo")))
                .andExpect(jsonPath("$.name", is("Lorenzo")))
                .andExpect(jsonPath("$.role", is("admin")));
    }

    @Test
    @DataSet(value = {"users.yml"}, cleanBefore = true)
    public void change_password_for_authenticated_user() throws Exception {
        this.mockMvc.perform(post("/v1/auth/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "currentPassword": "lorenzo",
                                  "newPassword": "nuovaPassword1"
                                }
                                """))
                .andExpect(status().isNoContent());

        this.mockMvc.perform(post("/v1/auth/login")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "lorenzo",
                                  "password": "nuovaPassword1"
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    @DataSet(value = {"users.yml"}, cleanBefore = true)
    public void logout_authenticated_user() throws Exception {
        this.mockMvc.perform(post("/v1/auth/logout"))
                .andExpect(status().isNoContent());
    }
}
