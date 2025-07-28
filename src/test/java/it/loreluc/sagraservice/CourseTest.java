package it.loreluc.sagraservice;

import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CourseTest extends CommonTest {

    @Test
    @DataSet( value = "courses.yml", cleanBefore = true)
    public void course_read_by_id() throws Exception {
        this.mockMvc.perform(get("/v1/courses/{id}", 1).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Primi")))
        ;
    }

    @Test
    @DataSet( value = "courses.yml", cleanBefore = true)
    public void course_read_not_found() throws Exception {
        this.mockMvc.perform(get("/v1/courses/{id}", 1111).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", notNullValue()))
        ;
    }

    @Test
    @DataSet( value = "courses.yml", cleanBefore = true)
    public void course_create() throws Exception {
        final String request = """
                {
                    "name": "Test"
                }
                """;
        this.mockMvc.perform(post("/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Test")))
        ;
    }

    @Test
    @DataSet( value = "courses.yml", cleanBefore = true)
    public void course_create_conflict() throws Exception {
        final String request = """
                {
                    "name": "Secondi"
                }
                """;
        this.mockMvc.perform(post("/v1/courses")
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
    @DataSet( value = "courses.yml", cleanBefore = true)
    public void course_delete() throws Exception {
        this.mockMvc.perform(delete("/v1/courses/{id}", 5).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent())
        ;
    }


    @Test
    @DataSet( value = {"courses.yml", "departments.yml","products.yml"}, cleanBefore = true)
    public void course_delete_conflict() throws Exception {
        this.mockMvc.perform(delete("/v1/courses/{id}", 1).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", notNullValue()))
        ;
    }

    @Test
    @DataSet( value = "courses.yml", cleanBefore = true)
    public void course_update() throws Exception {
        final String request = """
                {
                    "name": "Test"
                }
                """;
        this.mockMvc.perform(put("/v1/courses/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test")))
        ;
    }

    @Test
    @DataSet( value = "courses.yml", cleanBefore = true)
    public void course_update_conflict() throws Exception {
        final String request = """
                {
                    "name": "Test Test"
                }
                """;
        this.mockMvc.perform(put("/v1/courses/{id}", 1)
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
    @DataSet( value = "courses.yml", cleanBefore = true)
    public void course_search() throws Exception {
        this.mockMvc.perform(get("/v1/courses")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].name", is("Primi")))
                .andExpect(jsonPath("$[1].name", is("Secondi")))
                .andExpect(jsonPath("$[2].name", is("Panzanelle")))
                .andExpect(jsonPath("$[3].name", is("Bar")))
                .andExpect(jsonPath("$[4].name", is("Test Test")))
        ;
    }

    @Test
    @DataSet( value = "courses.yml", cleanBefore = true)
    public void course_search_by_name() throws Exception {
        this.mockMvc.perform(get("/v1/courses?name=Test")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Test Test")))
        ;
    }
}
