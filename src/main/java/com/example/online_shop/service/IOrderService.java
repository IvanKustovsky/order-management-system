package com.example.online_shop.service;

import com.example.online_shop.dto.CreateOrderRequestDto;
import com.example.online_shop.dto.OrderResponseDto;

import java.util.List;

public interface IOrderService {

    /**
     * Creates a new order based on the provided request details.
     *
     * @param request - Object containing the details of the order to be created (including user and order items)
     * @return OrderResponseDto - The details of the created order
     */
    OrderResponseDto createOrder(CreateOrderRequestDto request);

    /**
     * Retrieves the details of an order by its ID.
     *
     * @param orderId - The ID of the order to be retrieved
     * @return OrderResponseDto - The details of the order
     */
    OrderResponseDto getOrderById(Long orderId);

    /**
     * Cancels the order specified by the given order ID.
     *
     * @param orderId - The ID of the order to be canceled
     */
    void cancelOrder(Long orderId);

    /**
     * Retrieves a list of orders associated with a specific user by their user ID.
     *
     * @param userId - The ID of the user whose orders are to be fetched
     * @return List<OrderResponseDto> - A list of orders placed by the user
     */
    List<OrderResponseDto> getOrdersByUserId(Long userId);
}
