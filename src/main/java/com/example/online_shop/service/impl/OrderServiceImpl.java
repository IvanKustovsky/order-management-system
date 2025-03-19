package com.example.online_shop.service.impl;

import com.example.online_shop.dto.CreateOrderRequestDto;
import com.example.online_shop.dto.OrderItemRequestDto;
import com.example.online_shop.entity.Order;
import com.example.online_shop.entity.OrderItem;
import com.example.online_shop.entity.Product;
import com.example.online_shop.entity.UserEntity;
import com.example.online_shop.enums.OrderStatus;
import com.example.online_shop.exception.ProductOutOfStockException;
import com.example.online_shop.exception.ResourceNotFoundException;
import com.example.online_shop.repository.OrderRepository;
import com.example.online_shop.repository.ProductRepository;
import com.example.online_shop.repository.UserRepository;
import com.example.online_shop.service.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public Order createOrder(CreateOrderRequestDto request) {
        log.info("Creating order for user ID: {}", request.getUserId());

        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id",
                        String.valueOf(request.getUserId())));

        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.NEW)
                .orderItems(new ArrayList<>())
                .build();

        for (OrderItemRequestDto itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "Id",
                            String.valueOf(itemRequest.getProductId())));

            if (product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new ProductOutOfStockException(
                        product.getName(),
                        product.getStockQuantity(),
                        itemRequest.getQuantity()
                );
            }

            product.setStockQuantity(product.getStockQuantity() - itemRequest.getQuantity());
            productRepository.save(product); // update stock

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .build();

            order.getOrderItems().add(orderItem);
        }

        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "Id", String.valueOf(orderId)));
    }

    @Override
    public void cancelOrder(Long orderId) {
        Order order = getOrderById(orderId);

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order is already cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);

        // Повертаємо товари на склад
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        }

        orderRepository.save(order);
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findAllByUserId(userId);
    }
}
