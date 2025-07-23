package it.loreluc.sagraservice;

import com.github.database.rider.core.api.dataset.DataSet;
import it.loreluc.sagraservice.error.InvalidProduct;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductTest extends CommonTest {
    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml"}, cleanBefore = true)
    public void product_read_by_id() throws Exception {
        this.mockMvc.perform(get("/v1/products/{id}", 1).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Tordelli")))
                .andExpect(jsonPath("$.departmentId", is(1)))
                .andExpect(jsonPath("$.courseId", is(1)))
                .andExpect(jsonPath("$.price", is(8.0)))
                .andExpect(jsonPath("$.sellLocked", is(false)))
                .andExpect(jsonPath("$.initialQuantity", is(220)))
                .andExpect(jsonPath("$.availableQuantity", is(200)))
                .andExpect(jsonPath("$.created", notNullValue()))
                .andExpect(jsonPath("$.created", notNullValue()))
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml"}, cleanBefore = true)
    public void product_linked_read_by_id() throws Exception {
        this.mockMvc.perform(get("/v1/products/{id}", 7).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(7)))
                .andExpect(jsonPath("$.name", is("Tordelli con formaggio")))
                .andExpect(jsonPath("$.departmentId", is(3)))
                .andExpect(jsonPath("$.courseId", is(4)))
                .andExpect(jsonPath("$.price", is(2.0)))
                .andExpect(jsonPath("$.sellLocked", is(false)))
                .andExpect(jsonPath("$.initialQuantity", is(220)))
                .andExpect(jsonPath("$.availableQuantity", is(200)))
                .andExpect(jsonPath("$.parentId", is(1)))
                .andExpect(jsonPath("$.created", notNullValue()))
                .andExpect(jsonPath("$.created", notNullValue()))
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml"}, cleanBefore = true)
    public void product_read_not_found() throws Exception {
        this.mockMvc.perform(get("/v1/products/{id}", 1111).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", notNullValue()))
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml"}, cleanBefore = true)
    public void products_create() throws Exception {
        final String request = """
                {
                    "name": "  Nutellina  ",
                    "note": "buona da spalmare",
                    "departmentId": 1,
                    "courseId": 1,
                    "price": 1.5
                }
                """;
        this.mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Nutellina")))
                .andExpect(jsonPath("$.note", is("buona da spalmare")))
                .andExpect(jsonPath("$.departmentId", is(1)))
                .andExpect(jsonPath("$.courseId", is(1)))
                .andExpect(jsonPath("$.price", is(1.5)))
                .andExpect(jsonPath("$.sellLocked", is(false)))
                .andExpect(jsonPath("$.availableQuantity", is(0)))
                .andExpect(jsonPath("$.initialQuantity", is(0)))
                .andExpect(jsonPath("$.created", notNullValue()))
                .andExpect(jsonPath("$.created", notNullValue()))
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml"}, cleanBefore = true)
    public void course_create_linked() throws Exception {
        final String request = """
                {
                    "name": "Nutellina",
                    "note": "buona da spalmare",
                    "departmentId": 1,
                    "courseId": 1,
                    "price": 1.5,
                    "parentId": 1
                }
                """;
        this.mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Nutellina")))
                .andExpect(jsonPath("$.note", is("buona da spalmare")))
                .andExpect(jsonPath("$.departmentId", is(1)))
                .andExpect(jsonPath("$.courseId", is(1)))
                .andExpect(jsonPath("$.price", is(1.5)))
                .andExpect(jsonPath("$.sellLocked", is(false)))
                .andExpect(jsonPath("$.initialQuantity", is(220)))
                .andExpect(jsonPath("$.availableQuantity", is(200)))
                .andExpect(jsonPath("$.parentId", is(1)))
                .andExpect(jsonPath("$.created", notNullValue()))
                .andExpect(jsonPath("$.created", notNullValue()))
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml"}, cleanBefore = true)
    public void product_create_department_not_found() throws Exception {
        final String request = """
                {
                    "name": "Nutellina",
                    "note": "buona da spalmare",
                    "departmentId": 100,
                    "courseId": 1,
                    "price": 1.5
                }
                """;
        this.mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.invalidValues", hasSize(1)))
                .andExpect(jsonPath("$.invalidValues[0].field", is("departmentId")))
                .andExpect(jsonPath("$.invalidValues[0].value", is(100)))
                .andExpect(jsonPath("$.invalidValues[0].message", notNullValue()))
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml"}, cleanBefore = true)
    public void product_create_course_not_found() throws Exception {
        final String request = """
                {
                    "name": "Nutellina",
                    "note": "buona da spalmare",
                    "departmentId": 1,
                    "courseId": 100,
                    "price": 1.5
                }
                """;
        this.mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.invalidValues", hasSize(1)))
                .andExpect(jsonPath("$.invalidValues[0].field", is("courseId")))
                .andExpect(jsonPath("$.invalidValues[0].value", is(100)))
                .andExpect(jsonPath("$.invalidValues[0].message", notNullValue()))
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml"}, cleanBefore = true)
    public void product_create_parent_not_found() throws Exception {
        final String request = """
                {
                    "name": "Nutellina",
                    "note": "buona da spalmare",
                    "departmentId": 1,
                    "courseId": 1,
                    "parentId": 100,
                    "price": 1.5
                }
                """;
        this.mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.invalidValues", hasSize(1)))
                .andExpect(jsonPath("$.invalidValues[0].field", is("parentId")))
                .andExpect(jsonPath("$.invalidValues[0].value", is(100)))
                .andExpect(jsonPath("$.invalidValues[0].message", notNullValue()))
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml"}, cleanBefore = true)
    public void product_create_invalid_parent() throws Exception {
        final String request = """
                {
                    "name": "Nutellina",
                    "note": "buona da spalmare",
                    "departmentId": 1,
                    "courseId": 1,
                    "parentId": 7,
                    "price": 1.5
                }
                """;
        this.mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.invalidValues", hasSize(1)))
                .andExpect(jsonPath("$.invalidValues[0].field", is("parentId")))
                .andExpect(jsonPath("$.invalidValues[0].value", is(7)))
                .andExpect(jsonPath("$.invalidValues[0].message", notNullValue()))
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml"}, cleanBefore = true)
    public void product_create_conflict_name() throws Exception {
        final String request = """
                {
                    "name": "Tordelli",
                    "departmentId": 1,
                    "courseId": 1,
                    "price": 1.5
                }
                """;
        this.mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.invalidValues", hasSize(1)))
                .andExpect(jsonPath("$.invalidValues[0].field", is("name")))
                .andExpect(jsonPath("$.invalidValues[0].value", is("Tordelli")))
                .andExpect(jsonPath("$.invalidValues[0].message", notNullValue()))
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml"}, cleanBefore = true)
    public void product_delete() throws Exception {
        this.mockMvc.perform(delete("/v1/products/7")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent())
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
    //@ExpectedDataSet(value = {"courses.yml","departments.yml","products.yml","users.yml", "expected_orders_update_mixed.yml"})
    public void product_delete_conflict() throws Exception {
        this.mockMvc.perform(delete("/v1/products/1")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", notNullValue()))
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml"}, cleanBefore = true)
    public void product_sell_lock() throws Exception {
        this.mockMvc.perform(put("/v1/products/1/sellLock")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.sellLocked", is(true)))
        ;

        this.mockMvc.perform(put("/v1/products/1/sellUnlock")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.sellLocked", is(false)))
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml"}, cleanBefore = true)
    public void product_update_quantity() throws Exception {

        final int productId = 1;

        this.mockMvc.perform(get("/v1/products/{id}", productId).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productId)))
                .andExpect(jsonPath("$.initialQuantity", is(220)))
                .andExpect(jsonPath("$.availableQuantity", is(200)))
        ;

        final String request = """
                {
                    "quantityVariation": 10
                }
                """;
        this.mockMvc.perform(put("/v1/products/{id}/updateQuantity", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productId)))
                .andExpect(jsonPath("$.initialQuantity", is(230)))
                .andExpect(jsonPath("$.availableQuantity", is(210)))
        ;

        final String request2 = """
                {
                    "quantityVariation": -10
                }
                """;
        this.mockMvc.perform(put("/v1/products/{id}/updateQuantity", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request2)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productId)))
                .andExpect(jsonPath("$.initialQuantity", is(220)))
                .andExpect(jsonPath("$.availableQuantity", is(200)))
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml"}, cleanBefore = true)
    public void product_update_quantity_not_enough() throws Exception {

        final int productId = 1;

        final String request = """
                {
                    "quantityVariation": -1000
                }
                """;
        this.mockMvc.perform(put("/v1/products/{id}/updateQuantity", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(450))
                .andExpect(jsonPath("$.message", notNullValue()))
                .andExpect(jsonPath("$.invalidProducts", hasSize(1)))
                .andExpect(jsonPath("$.invalidProducts[0].productId", is(productId)))
                .andExpect(jsonPath("$.invalidProducts[0].error", is(InvalidProduct.ProductError.NOT_ENOUGH_QUANTITY.name())))
                .andExpect(jsonPath("$.invalidProducts[0].message", notNullValue()))
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml"}, cleanBefore = true)
    public void product_search() throws Exception {
        this.mockMvc.perform(get("/v1/products")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(9)))
                .andExpect(jsonPath("$[0].id", is(9))) // Bottiglia 0.5
                .andExpect(jsonPath("$[1].id", is(6))) // Bottiglia 1.5
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml"}, cleanBefore = true)
    public void product_search_by_name() throws Exception {
        this.mockMvc.perform(get("/v1/products?name=Tordelli")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Tordelli")))
                .andExpect(jsonPath("$[1].name", is("Tordelli con formaggio")))
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml"}, cleanBefore = true)
    public void product_search_exclude_linked() throws Exception {
        this.mockMvc.perform(get("/v1/products?excludeLinked=true")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(8)))
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml"}, cleanBefore = true)
    public void product_search_by_department() throws Exception {
        this.mockMvc.perform(get("/v1/products?departmentId=2")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Grigliata Rosticciana")))
                .andExpect(jsonPath("$[1].name", is("Grigliata Salsicce")))
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml"}, cleanBefore = true)
    public void product_search_by_course() throws Exception {
        this.mockMvc.perform(get("/v1/products?courseId=4")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml"}, cleanBefore = true)
    public void product_update() throws Exception {
        final String request = """
                {
                    "name": "Tordelli senza parmigiano",
                    "departmentId": 2,
                    "courseId": 2,
                    "price": 8.5
                }
                """;
        this.mockMvc.perform(put("/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Tordelli senza parmigiano")))
                .andExpect(jsonPath("$.note").doesNotExist())
                .andExpect(jsonPath("$.departmentId", is(2)))
                .andExpect(jsonPath("$.courseId", is(2)))
                .andExpect(jsonPath("$.price", is(8.5)))
                .andExpect(jsonPath("$.sellLocked", is(false)))
                .andExpect(jsonPath("$.initialQuantity", is(220)))
                .andExpect(jsonPath("$.availableQuantity", is(200)))
                .andExpect(jsonPath("$.parentId").doesNotExist())
                .andExpect(jsonPath("$.created", notNullValue()))
                .andExpect(jsonPath("$.created", notNullValue()))
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml"}, cleanBefore = true)
    public void product_update2() throws Exception {
        final String request = """
                {
                    "name": "Tordelli",
                    "departmentId": 2,
                    "courseId": 2,
                    "price": 8.5
                }
                """;
        this.mockMvc.perform(put("/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml"}, cleanBefore = true)
    public void product_update_conflict() throws Exception {
        final String request = """
                {
                    "name": "Tordelli con formaggio",
                    "departmentId": 2,
                    "courseId": 2,
                    "price": 8.5
                }
                """;
        this.mockMvc.perform(put("/v1/products/1")
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
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml", "orders.yml"}, cleanBefore = true)
    public void product_init() throws Exception {

        checkProductQuantity(1, 220, 200 );
        checkProductQuantity(2, 500, 500);
        checkProductQuantity(3, 75, 75);
        checkProductQuantity(4, 100, 100);
        checkProductQuantity(5, 30,30 );
        checkProductQuantity(6, 1000, 1000);
        checkProductQuantity(8, 100,100);

        final String request = """
                [
                    {
                       "productId": 1,
                       "initialQuantity": 150
                    },
                    {
                       "productId": 2,
                       "initialQuantity": 160
                    },
                    {
                       "productId": 3,
                       "initialQuantity": 170
                    },
                    {
                       "productId": 4,
                       "initialQuantity": 180
                    },
                    {
                       "productId": 5,
                       "initialQuantity": 190
                    },
                    {
                       "productId": 6,
                       "initialQuantity": 200
                    },
                    {
                       "productId": 8,
                       "initialQuantity": 210
                    }
                ]
                """;
        this.mockMvc.perform(put("/v1/products/initQuantity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", notNullValue()))

        ;

        checkProductQuantity(1, 150, 150 );
        checkProductQuantity(2, 160, 160);
        checkProductQuantity(3, 170, 170);
        checkProductQuantity(4, 180, 180);
        checkProductQuantity(5, 190,190);
        checkProductQuantity(6, 200, 200);
        checkProductQuantity(8, 210,210);
    }

    @Test
    @DataSet( value = {"courses.yml","departments.yml","products.yml","users.yml","today_order.yml"}, cleanBefore = true)
    public void product_init_conflict() throws Exception {

        final String request = """
                [
                    {
                       "productId": 1,
                       "initialQuantity": 150
                    },
                    {
                       "productId": 2,
                       "initialQuantity": 160
                    },
                    {
                       "productId": 3,
                       "initialQuantity": 170
                    },
                    {
                       "productId": 4,
                       "initialQuantity": 180
                    },
                    {
                       "productId": 5,
                       "initialQuantity": 190
                    },
                    {
                       "productId": 6,
                       "initialQuantity": 200
                    },
                    {
                       "productId": 8,
                       "initialQuantity": 210
                    }
                ]
                """;
        this.mockMvc.perform(put("/v1/products/initQuantity")
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
    @DataSet( value = {"courses.yml","departments.yml","products.yml"}, cleanBefore = true)
    public void product_init_parent_product() throws Exception {

        final String request = """
                [
                    {
                       "productId": 1,
                       "initialQuantity": 150
                    },
                    {
                       "productId": 2,
                       "initialQuantity": 160
                    },
                    {
                       "productId": 3,
                       "initialQuantity": 170
                    },
                    {
                       "productId": 4,
                       "initialQuantity": 180
                    },
                    {
                       "productId": 5,
                       "initialQuantity": 190
                    },
                    {
                       "productId": 6,
                       "initialQuantity": 200
                    },
                    {
                       "productId": 7,
                       "initialQuantity": 210
                    }
                ]
                """;
        this.mockMvc.perform(put("/v1/products/initQuantity")
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
    @DataSet( value = {"courses.yml","departments.yml","products.yml"}, cleanBefore = true)
    public void product_init_invalid_product() throws Exception {

        final String request = """
                [
                    {
                       "productId": 1,
                       "initialQuantity": 150
                    },
                    {
                       "productId": 2,
                       "initialQuantity": 160
                    },
                    {
                       "productId": 3,
                       "initialQuantity": 170
                    },
                    {
                       "productId": 4,
                       "initialQuantity": 180
                    },
                    {
                       "productId": 5,
                       "initialQuantity": 190
                    },
                    {
                       "productId": 6,
                       "initialQuantity": 200
                    },
                    {
                       "productId": 7777,
                       "initialQuantity": 210
                    }
                ]
                """;
        this.mockMvc.perform(put("/v1/products/initQuantity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", notNullValue()))
        ;
    }


    private void checkProductQuantity(int productId, int expectedInitialQuantity, int expectedAvailableQuantity) throws Exception {
        final MvcResult result = this.mockMvc.perform(get("/v1/products/{id}", productId).accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productId)))
                .andExpect(jsonPath("$.availableQuantity", is(expectedAvailableQuantity)))
                .andExpect(jsonPath("$.initialQuantity", is(expectedInitialQuantity)))
                .andReturn();

    }

}
