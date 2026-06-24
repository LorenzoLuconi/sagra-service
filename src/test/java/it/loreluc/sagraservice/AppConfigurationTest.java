package it.loreluc.sagraservice;

import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AppConfigurationTest extends CommonTest {

    @Test
    @DataSet(value = {"users.yml", "app_configurations.yml"}, cleanBefore = true)
    public void admin_can_read_configurations() throws Exception {
        this.mockMvc.perform(get("/v1/configurations").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].group", containsInAnyOrder("general", "order", "print")))
                .andExpect(jsonPath("$[?(@.group == 'general')].keys[*]", hasSize(4)))
                .andExpect(jsonPath("$[?(@.group == 'general')].keys[?(@.key == 'date-end')].type", contains("DATE")))
                .andExpect(jsonPath("$[?(@.group == 'general')].keys[?(@.key == 'date-end')].required", contains(false)))
                .andExpect(jsonPath("$[?(@.group == 'general')].keys[?(@.key == 'logo-svg')].type", contains("STRING")))
                .andExpect(jsonPath("$[?(@.group == 'general')].keys[?(@.key == 'logo-svg')].required", contains(true)));

        this.mockMvc.perform(get("/v1/configurations/general").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.group", is("general")))
                .andExpect(jsonPath("$.keys", hasSize(4)));

        this.mockMvc.perform(get("/v1/configurations/general/event-title").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key", is("event-title")))
                .andExpect(jsonPath("$.value", is("Sagra")))
                .andExpect(jsonPath("$.type", is("STRING")))
                .andExpect(jsonPath("$.required", is(true)))
                .andExpect(jsonPath("$.allowedValues", hasSize(0)));

        this.mockMvc.perform(get("/v1/configurations/print/split-by").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key", is("split-by")))
                .andExpect(jsonPath("$.required", is(true)))
                .andExpect(jsonPath("$.allowedValues", hasSize(3)))
                .andExpect(jsonPath("$.allowedValues[0]", is("none")))
                .andExpect(jsonPath("$.allowedValues[1]", is("course")))
                .andExpect(jsonPath("$.allowedValues[2]", is("department")));
    }

    @Test
    @DataSet(value = {"users.yml", "app_configurations.yml"}, cleanBefore = true)
    public void admin_can_update_configuration_value() throws Exception {
        this.mockMvc.perform(put("/v1/configurations/general/event-title")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "value": "Sagra 2026"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key", is("event-title")))
                .andExpect(jsonPath("$.value", is("Sagra 2026")));
    }

    @Test
    @DataSet(value = {"users.yml", "app_configurations.yml"}, cleanBefore = true)
    public void admin_can_update_configuration_group() throws Exception {
        this.mockMvc.perform(put("/v1/configurations/general")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "keys": [
                                    { "key": "event-title", "value": "Sagra 2026" },
                                    { "key": "date-start", "value": "2026-06-01" }
                                  ]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.group", is("general")))
                .andExpect(jsonPath("$.keys[?(@.key == 'date-start')].value", contains("2026-06-01")))
                .andExpect(jsonPath("$.keys[?(@.key == 'event-title')].value", contains("Sagra 2026")));
    }

    @Test
    @DataSet(value = {"users.yml", "app_configurations.yml"}, cleanBefore = true)
    public void admin_can_update_all_configurations() throws Exception {
        this.mockMvc.perform(put("/v1/configurations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "groups": [
                                    {
                                      "group": "general",
                                      "keys": [
                                        { "key": "event-title", "value": "Sagra 2026" }
                                      ]
                                    },
                                    {
                                      "group": "order",
                                      "keys": [
                                        { "key": "take-away-enabled", "value": "false" }
                                      ]
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].group", containsInAnyOrder("general", "order", "print")))
                .andExpect(jsonPath("$[?(@.group == 'general')].keys[?(@.key == 'event-title')].value", contains("Sagra 2026")))
                .andExpect(jsonPath("$[?(@.group == 'order')].keys[?(@.key == 'take-away-enabled')].value", contains("false")));
    }

    @Test
    @DataSet(value = {"users.yml", "app_configurations.yml"}, cleanBefore = true)
    public void admin_can_update_required_svg_configuration() throws Exception {
        this.mockMvc.perform(put("/v1/configurations/general/logo-svg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "value": "<svg xmlns=\\"http://www.w3.org/2000/svg\\"><path d=\\"M0 0h10v10H0z\\"/></svg>"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key", is("logo-svg")))
                .andExpect(jsonPath("$.type", is("STRING")))
                .andExpect(jsonPath("$.required", is(true)));

        this.mockMvc.perform(put("/v1/configurations/general/logo-svg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "value": null
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"users.yml", "app_configurations.yml"}, cleanBefore = true)
    public void invalid_configuration_value_returns_bad_request() throws Exception {
        this.mockMvc.perform(put("/v1/configurations/print/split-by")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "value": "unsupported"
                                }
                                """))
                .andExpect(status().isBadRequest());

        this.mockMvc.perform(put("/v1/configurations/order/service-cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "value": "abc"
                                }
                                """))
                .andExpect(status().isBadRequest());

        this.mockMvc.perform(put("/v1/configurations/general/date-start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "value": "01/06/2026"
                                }
                                """))
                .andExpect(status().isBadRequest());

        this.mockMvc.perform(put("/v1/configurations/general/event-title")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "value": null
                                }
                                """))
                .andExpect(status().isBadRequest());

        this.mockMvc.perform(put("/v1/configurations/general/event-title")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "value": "   "
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DataSet(value = {"users.yml", "app_configurations.yml"}, cleanBefore = true)
    public void optional_date_configurations_can_be_set_to_null() throws Exception {
        this.mockMvc.perform(put("/v1/configurations/general/date-start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "value": null
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key", is("date-start")))
                .andExpect(jsonPath("$.required", is(false)));
    }

    @Test
    @DataSet(value = {"users.yml", "app_configurations.yml"}, cleanBefore = true)
    public void unknown_configuration_returns_not_found() throws Exception {
        this.mockMvc.perform(get("/v1/configurations/general/unknown").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test", roles = {"CASHIER"})
    @DataSet(value = {"users.yml", "app_configurations.yml"}, cleanBefore = true)
    public void cashier_can_read_configuration() throws Exception {
        this.mockMvc.perform(get("/v1/configurations").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test", roles = {"CASHIER"})
    @DataSet(value = {"users.yml", "app_configurations.yml"}, cleanBefore = true)
    public void cashier_cannot_update_configuration() throws Exception {
        this.mockMvc.perform(put("/v1/configurations/general")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "keys": [
                                    { "key": "event-title", "value": "Sagra 2026" },
                                    { "key": "date-start", "value": "2026-06-01" }
                                  ]
                                }
                                """))
                .andExpect(status().isForbidden());
    }
}
