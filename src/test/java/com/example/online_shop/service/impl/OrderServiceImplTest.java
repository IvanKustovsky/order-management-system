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
import com.example.online_shop.repository.OrderRepository;
import com.example.online_shop.repository.ProductRepository;
import com.example.online_shop.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Order Service Test Class")
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void createOrder_Success() {
        log.info("Test: createOrder_Success");

        Long userId = 1L;
        Long productId = 10L;
        int quantity = 2;

        CreateOrderRequestDto request = CreateOrderRequestDto.builder()
                .userId(userId)
                .items(List.of(
                        OrderItemRequestDto.builder()
                                .productId(productId)
                                .quantity(quantity)
                                .build()
                ))
                .build();

        UserEntity user = UserEntity.builder().id(userId).build();
        Product product = Product.builder().id(productId).stockQuantity(5).name("Test Product").build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            o.setId(100L);
            return o;
        });

        OrderResponseDto responseDto = orderService.createOrder(request);

        assertNotNull(responseDto);
        assertEquals(100L, responseDto.getId());
        verify(orderRepository).save(any(Order.class));
        verify(productRepository).save(any(Product.class));

        log.info("Order created successfully with ID: {}", responseDto.getId());
    }

    @Test
    void createOrder_UserNotFound_ThrowsException() {
        log.info("Test: createOrder_UserNotFound_ThrowsException");

        Long userId = 1L;
        CreateOrderRequestDto request = CreateOrderRequestDto.builder()
                .userId(userId)
                .items(Collections.emptyList())
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(request));

        log.info("ResourceNotFoundException was thrown as expected");
    }

    @Test
    void createOrder_ProductNotFound_ThrowsException() {
        log.info("Test: createOrder_ProductNotFound_ThrowsException");

        Long userId = 1L;
        Long productId = 10L;

        CreateOrderRequestDto request = CreateOrderRequestDto.builder()
                .userId(userId)
                .items(List.of(OrderItemRequestDto.builder()
                        .productId(productId)
                        .quantity(1)
                        .build()))
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity()));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(request));

        log.info("ResourceNotFoundException was thrown as expected for product");
    }

    @Test
    void createOrder_ProductOutOfStock_ThrowsException() {
        log.info("Test: createOrder_ProductOutOfStock_ThrowsException");

        Long userId = 1L;
        Long productId = 10L;

        CreateOrderRequestDto request = CreateOrderRequestDto.builder()
                .userId(userId)
                .items(List.of(OrderItemRequestDto.builder()
                        .productId(productId)
                        .quantity(10)
                        .build()))
                .build();

        Product product = Product.builder()
                .id(productId)
                .stockQuantity(5)
                .name("Test Product")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity()));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        assertThrows(ProductOutOfStockException.class, () -> orderService.createOrder(request));

        log.info("ProductOutOfStockException was thrown as expected");
    }

    @Test
    void getOrderById_Success() {
        log.info("Test: getOrderById_Success");

        Long orderId = 100L;
        Order order = Order.builder().id(orderId).build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        OrderResponseDto responseDto = orderService.getOrderById(orderId);

        assertNotNull(responseDto);
        assertEquals(orderId, responseDto.getId());

        log.info("Order retrieved successfully with ID: {}", responseDto.getId());
    }

    @Test
    void getOrderById_OrderNotFound_ThrowsException() {
        log.info("Test: getOrderById_OrderNotFound_ThrowsException");

        Long orderId = 100L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(orderId));

        log.info("ResourceNotFoundException was thrown as expected");
    }

    @Test
    void getOrdersByUserId_Success() {
        log.info("Test: getOrdersByUserId_Success");

        Long userId = 1L;
        List<Order> orders = List.of(Order.builder().id(1L).build(), Order.builder().id(2L).build());

        when(orderRepository.findAllByUserId(userId)).thenReturn(orders);

        List<OrderResponseDto> result = orderService.getOrdersByUserId(userId);

        assertEquals(2, result.size());

        log.info("Orders retrieved successfully for user ID: {}", userId);
    }

    @Test
    void getOrdersByUserId_NoOrders_ThrowsException() {
        log.info("Test: getOrdersByUserId_NoOrders_ThrowsException");

        Long userId = 1L;

        when(orderRepository.findAllByUserId(userId)).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrdersByUserId(userId));

        log.info("ResourceNotFoundException was thrown as expected for user orders");
    }

    @Test
    void cancelOrder_Success() {
        log.info("Test: cancelOrder_Success");

        Long orderId = 100L;
        Product product = Product.builder().id(10L).stockQuantity(5).build();
        OrderItem item = OrderItem.builder().product(product).quantity(2).build();
        Order order = Order.builder().id(orderId).status(OrderStatus.NEW).orderItems(List.of(item)).build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.cancelOrder(orderId);

        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        assertEquals(7, product.getStockQuantity());

        verify(orderRepository).save(order);
        verify(productRepository).save(product);

        log.info("Order cancelled successfully with ID: {}", orderId);
    }

    @Test
    void cancelOrder_AlreadyCancelled_ThrowsException() {
        log.info("Test: cancelOrder_AlreadyCancelled_ThrowsException");

        Long orderId = 100L;
        Order order = Order.builder().id(orderId).status(OrderStatus.CANCELLED).build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(OrderAlreadyCancelledException.class, () -> orderService.cancelOrder(orderId));

        log.info("OrderAlreadyCancelledException was thrown as expected");
    }

    @Test
    void cancelOrder_OrderNotFound_ThrowsException() {
        log.info("Test: cancelOrder_OrderNotFound_ThrowsException");

        Long orderId = 100L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.cancelOrder(orderId));

        log.info("ResourceNotFoundException was thrown as expected for cancelling order");
    }
}