package com.example.online_shop.controller;


import com.example.online_shop.constants.ProductConstants;
import com.example.online_shop.dto.ErrorResponseDto;
import com.example.online_shop.dto.ProductDto;
import com.example.online_shop.dto.ResponseDto;
import com.example.online_shop.entity.Product;
import com.example.online_shop.service.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(
        name = "Product Management",
        description = "APIs for managing products in Order Management System"
)
@RestController
@RequestMapping(path = "/products/api/v1", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "${cross-origin.allowed-origin:http://localhost:3000}")
public class ProductController {

    private final IProductService productService;

    @Operation(
            summary = "Add a new product",
            description = "Creates a new product in the Order Management System, optionally with an image"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product successfully added"),
            @ApiResponse(responseCode = "401", description = "User Unauthorized"),
            @ApiResponse(responseCode = "400", description = "Bad request - Invalid product details",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto> addProduct(
            @RequestPart("productDto") @Valid ProductDto productDto,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        productService.addProduct(productDto, image);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(ProductConstants.STATUS_201, ProductConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Get all products",
            description = "Fetches a list of all available products"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping("/all")
    public ResponseEntity<List<Product>> fetchProducts() {
        var products = productService.getAllProducts();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(products);
    }

    @Operation(
            summary = "Delete a product",
            description = "Deletes a product by its ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product successfully deleted"),
            @ApiResponse(responseCode = "401", description = "User Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto> deleteProduct(@PathVariable(name = "id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(ProductConstants.STATUS_200, ProductConstants.MESSAGE_200));
    }
}
