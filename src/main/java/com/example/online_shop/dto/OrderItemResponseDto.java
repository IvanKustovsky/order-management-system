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
@Schema(name = "OrderItemResponseDto", description = "DTO for an individual order item response, including product details and quantity")
public class OrderItemResponseDto {

    @Schema(description = "Unique ID of the order item", example = "1")
    private Long id;

    @Schema(description = "Product details for the order item", implementation = ProductShortDto.class)
    private ProductShortDto product; // minimizes nesting

    @Schema(description = "Quantity of the product in the order item", example = "2")
    private Integer quantity;
}
