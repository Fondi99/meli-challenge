package mfondini.meli.challenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import mfondini.meli.challenge.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllProducts() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldReturnSingleProduct() throws Exception {
        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldReturn404ForMissingProduct() throws Exception {
        mockMvc.perform(get("/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void shouldCompareProducts() throws Exception {
        mockMvc.perform(post("/products/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[1,2]"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateProduct() throws Exception {
        Product newProduct = new Product(0, "Test Laptop", "url", "desc", 1000.0, 4.5, Map.of("cpu","i7"));
        String json = objectMapper.writeValueAsString(newProduct);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Laptop"));
    }

    @Test
    void shouldReturn400ForInvalidProduct() throws Exception {
        Product invalidProduct = new Product(0, "", "url", "desc", -50.0, 6.0, Map.of());
        String json = objectMapper.writeValueAsString(invalidProduct);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").value("Name cannot be empty"))
                .andExpect(jsonPath("$.errors.price").value("Price must be a positive number"))
                .andExpect(jsonPath("$.errors.rating").value("Rating cannot be greater than 5"));
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        Product updated = new Product(1, "Updated Laptop", "url", "desc", 1200.0, 4.8, Map.of());
        String json = objectMapper.writeValueAsString(updated);

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Laptop"));
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isNoContent());
    }
}
