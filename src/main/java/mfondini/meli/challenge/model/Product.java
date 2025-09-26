package mfondini.meli.challenge.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Integer id;
    @NotBlank(message = "Name cannot be empty")
    private String name;
    private String imageUrl;
    private String description;
    @Min(value = 0, message = "Price must be a positive number")
    private Double price;
    @NotNull(message = "Rating is required")
    @Min(value = 0, message = "Rating must be at least 0")
    @Max(value = 5, message = "Rating cannot be greater than 5")
    private Double rating;
    private Map<String, String> specifications;
}