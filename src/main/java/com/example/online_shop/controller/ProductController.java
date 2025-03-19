package com.example.online_shop.controller;


import com.example.online_shop.constants.ProductConstants;
import com.example.online_shop.dto.ProductDto;
import com.example.online_shop.dto.ResponseDto;
import com.example.online_shop.entity.Product;
import com.example.online_shop.service.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/products/api/v1", produces = {MediaType.APPLICATION_JSON_VALUE})
@Slf4j
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final IProductService productService;

    @PostMapping(value = "/add")
    public ResponseEntity<ResponseDto> addProduct(@RequestPart("productDto") @Valid ProductDto productDto,
                                                  @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        log.info("Received request to add product: {}", productDto);

        if (image != null) {
            log.info("Received image: name={}, size={} bytes", image.getOriginalFilename(), image.getSize());
        } else {
            log.warn("No image was uploaded with the product");
        }

        productService.addProduct(productDto, image);

        log.info("Product successfully added!");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(ProductConstants.STATUS_201, ProductConstants.MESSAGE_201));
    }

    @GetMapping
    public ResponseEntity<List<Product>> fetchUser() {
        var products = productService.getAllProducts();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(products);
    }

    //@PreAuthorize("hasRole('ADMIN')") TODO
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteProduct(@PathVariable(name = "id") Long id) {
        productService.deleteProduct(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(ProductConstants.STATUS_200, ProductConstants.MESSAGE_200));
    }
}
