package com.example.online_shop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "CreateOrderRequestDto", description = "DTO for creating an order with user information and items")
public class CreateOrderRequestDto {

    @Schema(description = "ID of the user placing the order", example = "12345")
    private Long userId;

    @Schema(description = "List of items in the order", example = "[{\"productId\": 1, \"quantity\": 2}]")
    @NotEmpty(message = "Order must contain at least one item")
    private List<OrderItemRequestDto> items;
}