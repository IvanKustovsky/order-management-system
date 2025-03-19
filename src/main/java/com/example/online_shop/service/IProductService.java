package com.example.online_shop.service;


import com.example.online_shop.dto.ProductDto;
import com.example.online_shop.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProductService {

    /**
     * @param productDto - ProductDto Object
     */
    void addProduct(ProductDto productDto, MultipartFile image) throws IOException;

    /**
     * @return all products
     */
    List<Product> getAllProducts();

    /**
     *
     * @param productId - Input productId
     */
    void deleteProduct(Long productId);
}
