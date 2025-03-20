package com.example.online_shop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ProductShortDto", description = "DTO for minimal product details including ID, name, and price")
public class ProductShortDto {

    @Schema(description = "Unique ID of the product", example = "1")
    private Long id;

    @Schema(description = "Name of the product", example = "Laptop")
    private String name;

    @Schema(description = "Price of the product", example = "999.99")
    private Double price;
}
