package mfondini.meli.challenge;

import mfondini.meli.challenge.exception.ResourceNotFoundException;
import mfondini.meli.challenge.model.Product;
import mfondini.meli.challenge.repository.ProductRepository;
import mfondini.meli.challenge.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductRepository repository;
    private ProductService service;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(ProductRepository.class);
        service = new ProductService(repository);
    }

    @Test
    void shouldReturnProductWhenExists() {
        Product mockProduct = new Product(1, "Laptop Pro", "url", "desc", 1000.0, 4.5, Map.of("cpu", "i7"));
        when(repository.findById(1)).thenReturn(Optional.of(mockProduct));

        Product result = service.getProductById(1);

        assertNotNull(result);
        assertEquals("Laptop Pro", result.getName());
    }

    @Test
    void shouldThrowWhenProductNotFound() {
        when(repository.findById(99)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getProductById(99));
    }

    @Test
    void shouldReturnAllProducts() {
        when(repository.findAll()).thenReturn(List.of(
                new Product(1, "Laptop A", "url", "desc", 500d, 4.0, Map.of()),
                new Product(2, "Laptop B", "url", "desc", 700d, 4.2, Map.of())
        ));
        List<Product> result = service.getAllProducts();
        assertEquals(2, result.size());
    }

    @Test
    void shouldCreateProduct() {
        Product product = new Product(0, "Laptop X", "url", "desc", 900.0, 4.0, Map.of());
        Product saved = new Product(1, "Laptop X", "url", "desc", 900.0, 4.0, Map.of());
        when(repository.save(product)).thenReturn(saved);

        Product result = service.createProduct(product);
        assertEquals(1, result.getId());
    }

    @Test
    void shouldUpdateProduct() {
        Product updated = new Product(1, "Updated Laptop", "url", "desc", 1000.0, 4.5, Map.of());
        when(repository.update(1, updated)).thenReturn(updated);

        Product result = service.updateProduct(1, updated);
        assertEquals("Updated Laptop", result.getName());
    }

    @Test
    void shouldThrowWhenUpdatingMissingProduct() {
        Product updated = new Product(99, "Missing", "url", "desc", 100.0, 3.0, Map.of());
        when(repository.update(99, updated)).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> service.updateProduct(99, updated));
    }

    @Test
    void shouldDeleteProduct() {
        doNothing().when(repository).deleteById(1);
        assertDoesNotThrow(() -> service.deleteProduct(1));
    }

    @Test
    void shouldThrowWhenDeletingMissingProduct() {
        doThrow(new ResourceNotFoundException("Product not found with id 99"))
                .when(repository).deleteById(99);
        assertThrows(ResourceNotFoundException.class, () -> service.deleteProduct(99));
    }

}
