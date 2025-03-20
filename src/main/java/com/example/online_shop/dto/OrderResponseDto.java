package com.example.online_shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {

    private Long id;
    private LocalDateTime orderDate;
    private String status;
    private UserShortDto user;
    private List<OrderItemResponseDto> orderItems;
}
