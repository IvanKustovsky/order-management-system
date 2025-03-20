package com.example.online_shop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "OrderItemRequestDto", description = "DTO for an individual item in the order, including product ID and quantity")
public class OrderItemRequestDto {

    @Schema(description = "ID of the product in the order", example = "1")
    @NotNull(message = "Product ID is required")
    private Long productId;

    @Schema(description = "Quantity of the product in the order", example = "2")
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
