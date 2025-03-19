package com.example.online_shop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Schema(name = "Product", description = "Schema to hold Product information")
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    @Schema(description = "Name of the product", example = "Laptop")
    @NotEmpty(message = "Name cannot be null or empty")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @Schema(description = "Description of the product", example = "Laptop Asus")
    @NotEmpty(message = "Description cannot be null or empty")
    @Size(max = 255, message = "Description must be less than 255 characters")
    private String description;

    @Schema(description = "Price of the product", example = "999.99")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double price;

    @Min(value = 0, message = "Stock quantity cannot be negative")
    @Schema(description = "Available quantity in stock", example = "10")
    private Integer stockQuantity;
}
