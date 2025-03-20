package com.example.online_shop.controller;

import com.example.online_shop.dto.CreateOrderRequestDto;
import com.example.online_shop.dto.ErrorResponseDto;
import com.example.online_shop.dto.OrderResponseDto;
import com.example.online_shop.service.IOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(
        name = "Order Management",
        description = "APIs for managing orders in Order Management System"
)
@RestController
@RequestMapping("/orders/api/v1")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "${cross-origin.allowed-origin:http://localhost:3000}")
public class OrderController {

    private final IOrderService orderService;

    @Operation(
            summary = "Create a new order",
            description = "Creates a new order in the Order Management System"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order successfully created"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid order details",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/create")
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody CreateOrderRequestDto request) {
        OrderResponseDto orderResponseDto = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDto);
    }

    @Operation(
            summary = "Get order details by ID",
            description = "Fetches order details by order ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "User Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long orderId) {
        OrderResponseDto orderResponseDto = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderResponseDto);
    }

    @Operation(
            summary = "Cancel an order",
            description = "Cancels an existing order by order ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order cancelled successfully"),
            @ApiResponse(responseCode = "401", description = "User Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok("Order cancelled successfully");
    }

    @Operation(
            summary = "Get all orders for a user",
            description = "Fetches all orders for a given user ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "User Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderResponseDto> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }
}
