package com.example.online_shop.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequestDto {
    private Long userId;

    @NotEmpty(message = "Order must contain at least one item")
    private List<OrderItemRequestDto> items;
}
