package com.example.online_shop.service.impl;

import com.example.online_shop.dto.CreateOrderRequestDto;
import com.example.online_shop.dto.OrderItemRequestDto;
import com.example.online_shop.dto.OrderResponseDto;
import com.example.online_shop.entity.Order;
import com.example.online_shop.entity.OrderItem;
import com.example.online_shop.entity.Product;
import com.example.online_shop.entity.UserEntity;
import com.example.online_shop.enums.OrderStatus;
import com.example.online_shop.exception.OrderAlreadyCancelledException;
import com.example.online_shop.exception.ProductOutOfStockException;
import com.example.online_shop.exception.ResourceNotFoundException;
import com.example.online_shop.mapper.OrderMapper;
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
    @Transactional
    public OrderResponseDto createOrder(CreateOrderRequestDto request) {
        log.info("Creating order for user ID: {}", request.getUserId());

        UserEntity user = getUserById(request.getUserId());
        log.info("User found: {}", user.getEmail());

        Order order = createOrderEntity(user);
        log.info("Order entity created with status: {}", order.getStatus());

        request.getItems().forEach(itemRequest -> processOrderItem(itemRequest, order));

        Order savedOrder = orderRepository.save(order);
        log.info("Order successfully saved with ID: {}", savedOrder.getId());

        return OrderMapper.INSTANCE.toOrderResponseDto(savedOrder);
    }

    private UserEntity getUserById(Long userId) {
        log.info("Fetching user by ID: {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User with ID {} not found", userId);
                    return new ResourceNotFoundException("User", "Id", String.valueOf(userId));
                });
    }

    private Order createOrderEntity(UserEntity user) {
        return Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.NEW)
                .orderItems(new ArrayList<>())
                .build();
    }

    private void processOrderItem(OrderItemRequestDto itemRequest, Order order) {
        log.info("Processing order item for product ID: {}", itemRequest.getProductId());

        Product product = productRepository.findById(itemRequest.getProductId())
                .orElseThrow(() -> {
                    log.error("Product with ID {} not found", itemRequest.getProductId());
                    return new ResourceNotFoundException("Product", "Id", String.valueOf(itemRequest.getProductId()));
                });

        if (product.getStockQuantity() < itemRequest.getQuantity()) {
            log.error("Product '{}' is out of stock. Available: {}, Requested: {}",
                    product.getName(), product.getStockQuantity(), itemRequest.getQuantity());
            throw new ProductOutOfStockException(
                    product.getName(), product.getStockQuantity(), itemRequest.getQuantity()
            );
        }

        product.setStockQuantity(product.getStockQuantity() - itemRequest.getQuantity());
        productRepository.save(product);
        log.info("Stock updated for product '{}'. New quantity: {}", product.getName(), product.getStockQuantity());

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(itemRequest.getQuantity())
                .build();

        order.getOrderItems().add(orderItem);
        log.info("Added order item: Product ID: {}, Quantity: {}", product.getId(), itemRequest.getQuantity());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(Long orderId) {
        log.info("Fetching order by ID: {}", orderId);
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Order with ID {} not found", orderId);
                    return new ResourceNotFoundException("Order", "Id", String.valueOf(orderId));
                });
        return OrderMapper.INSTANCE.toOrderResponseDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrdersByUserId(Long userId) {
        log.info("Fetching orders for user ID: {}", userId);
        List<Order> orders = orderRepository.findAllByUserId(userId);

        if (orders.isEmpty()) {
            log.warn("No orders found for user ID: {}", userId);
            throw new ResourceNotFoundException("Orders", "UserId", String.valueOf(userId));
        }

        return orders.stream()
                .map(OrderMapper.INSTANCE::toOrderResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        log.info("Cancelling order with ID: {}", orderId);
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Order with ID {} not found", orderId);
                    return new ResourceNotFoundException("Order", "Id", String.valueOf(orderId));
                });

        if (order.getStatus() == OrderStatus.CANCELLED) {
            log.warn("Order with ID {} is already cancelled", orderId);
            throw new OrderAlreadyCancelledException();
        }

        order.setStatus(OrderStatus.CANCELLED);
        log.info("Order status updated to CANCELLED for order ID: {}", orderId);

        // Restore stock for cancelled order
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
            log.info("Restored stock for product '{}'. New quantity: {}", product.getName(), product.getStockQuantity());
        }

        orderRepository.save(order);
        log.info("Order with ID {} successfully cancelled", orderId);
    }
}
