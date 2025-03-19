package com.example.online_shop.service;

import com.example.online_shop.dto.CreateOrderRequestDto;
import com.example.online_shop.entity.Order;

import java.util.List;

public interface IOrderService {

    Order createOrder(CreateOrderRequestDto request);

    Order getOrderById(Long orderId);

    void cancelOrder(Long orderId);

    List<Order> getOrdersByUserId(Long userId);
}
