package com.example.online_shop.service.impl;

import com.example.online_shop.dto.ProductDto;
import com.example.online_shop.entity.Product;
import com.example.online_shop.mapper.ProductMapper;
import com.example.online_shop.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Product Service Test Class")
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    @DisplayName("Should add product with image successfully")
    @Order(1)
    void addProduct_WithImage_Success() throws IOException {
        // Arrange
        ProductDto productDto = ProductDto.builder()
                .name("Test Product")
                .description("Test Description")
                .price(100.0)
                .build();

        MultipartFile image = new MockMultipartFile(
                "image",
                "test_image.png",
                "image/png",
                "fake-image-content".getBytes()
        );

//        ProductMapper mapper = mock(ProductMapper.class);
//        Product product = Product.builder().id(1L).name("Test Product").build();
//        // Ти можеш використати свій ProductMapper.INSTANCE.toEntity, або мокнути як тут.

        // Act
        productService.addProduct(productDto, image);

        // Assert
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should add product without image and set imagePath as null")
    @Order(2)
    void addProduct_WithoutImage_WarningAndSuccess() throws IOException {
        // Arrange
        ProductDto productDto = ProductDto.builder()
                .name("Product Without Image")
                .description("Description")
                .price(50.0)
                .build();

        MultipartFile image = null;

        // Act
        productService.addProduct(productDto, image);

        // Assert
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should throw IOException when image has empty original filename")
    @Order(3)
    void addProduct_WithImage_EmptyFileName_ThrowsIOException() {
        // Arrange
        ProductDto productDto = ProductDto.builder()
                .name("Invalid Image Product")
                .description("Invalid Image Description")
                .price(30.0)
                .build();

        MultipartFile image = new MockMultipartFile(
                "image",
                "",
                "image/png",
                "fake-image-content".getBytes()
        );

        // Act & Assert
        assertThatThrownBy(() -> productService.addProduct(productDto, image))
                .isInstanceOf(IOException.class)
                .hasMessageContaining("Uploaded image has no file name");

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Should get all products")
    @Order(4)
    void getAllProducts_Success() {
        // Arrange
        List<Product> mockProductList = Arrays.asList(
                Product.builder().id(1L).name("Product 1").build(),
                Product.builder().id(2L).name("Product 2").build()
        );

        when(productRepository.findAll()).thenReturn(mockProductList);

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertEquals(2, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should delete product by ID")
    @Order(5)
    void deleteProduct_Success() {
        // Arrange
        Long productId = 1L;

        // Act
        productService.deleteProduct(productId);

        // Assert
        verify(productRepository, times(1)).deleteById(productId);
    }
}