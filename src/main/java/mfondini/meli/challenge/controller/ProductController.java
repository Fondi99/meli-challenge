package mfondini.meli.challenge.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import mfondini.meli.challenge.model.Product;
import mfondini.meli.challenge.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        //Return all products from the JSON file
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Integer id) {
        //Return a single product from the JSON file searched by id
        return productService.getProductById(id);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        //Create a new Product
        Product created = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @Valid @RequestBody Product product) {
        //Update a product by id
        Product updated = productService.updateProduct(id, product);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        //Delete a product by id
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/compare")
    public List<Product> compareProducts(@RequestBody List<Integer> productIds) {
        //Return a list of product details from a list of product ids
        //I'm assuming the comparison feature is done by FE, so I think a list is the best way for returning the data
        return productIds.stream()
                .map(productService::getProductById)
                .collect(Collectors.toList());
    }
}