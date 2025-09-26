package mfondini.meli.challenge.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import mfondini.meli.challenge.exception.ResourceNotFoundException;
import mfondini.meli.challenge.model.Product;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class ProductRepository {

    private static final String FILE_PATH = "src/main/resources/products.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<Product> products;

    public ProductRepository() {
        try {
            File file = new File(FILE_PATH);
            if (file.exists()) {
                products = objectMapper.readValue(file, new TypeReference<List<Product>>() {});
            } else {
                products = new ArrayList<>();
                saveAll();
            }
        } catch (IOException e) {
            log.error("Hubo un error al inicializar el archivo de guardado: {}", e.getMessage());
            products = new ArrayList<>();
        }
    }

    public List<Product> findAll() {
        return new ArrayList<>(products);
    }

    public Optional<Product> findById(int id) {
        return products.stream().filter(p -> p.getId() == id).findFirst();
    }

    public Product save(Product product) {
        // Generar ID incremental
        int nextId = products.stream().mapToInt(Product::getId).max().orElse(0) + 1;
        product.setId(nextId);
        products.add(product);
        saveAll();
        return product;
    }

    public Product update(int id, Product updatedProduct) {
        Product existing = findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));

        if (updatedProduct.getName() != null) existing.setName(updatedProduct.getName());
        if (updatedProduct.getDescription() != null) existing.setDescription(updatedProduct.getDescription());
        if (updatedProduct.getImageUrl() != null) existing.setImageUrl(updatedProduct.getImageUrl());
        if (updatedProduct.getPrice() != null) existing.setPrice(updatedProduct.getPrice());
        if (updatedProduct.getRating() != null) existing.setRating(updatedProduct.getRating());
        if (updatedProduct.getSpecifications() != null) existing.setSpecifications(updatedProduct.getSpecifications());

        saveAll();
        return existing;
    }

    public void deleteById(int id) {
        boolean removed = products.removeIf(p -> p.getId() == id);
        if (!removed) throw new ResourceNotFoundException("Product not found with id " + id);
        saveAll();
    }

    private void saveAll() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), products);
        } catch (IOException e) {
            throw new RuntimeException("Error saving products to file", e);
        }
    }
}
