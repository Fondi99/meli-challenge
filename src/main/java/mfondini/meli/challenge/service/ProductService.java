package mfondini.meli.challenge.service;

import lombok.AllArgsConstructor;
import mfondini.meli.challenge.exception.ResourceNotFoundException;
import mfondini.meli.challenge.model.Product;
import mfondini.meli.challenge.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Product getProductById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
    }

    public Product createProduct(Product product) {
        return repository.save(product);
    }

    public Product updateProduct(int id, Product updatedProduct) {
        Product result = repository.update(id, updatedProduct);
        if (result == null) {
            throw new ResourceNotFoundException("Product not found with id " + id);
        }
        return result;
    }

    public void deleteProduct(int id) {
        repository.deleteById(id);
    }
}