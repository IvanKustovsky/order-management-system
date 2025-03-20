package com.example.online_shop.service.impl;

import com.example.online_shop.dto.ProductDto;
import com.example.online_shop.entity.Product;
import com.example.online_shop.mapper.ProductMapper;
import com.example.online_shop.repository.ProductRepository;
import com.example.online_shop.service.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void addProduct(ProductDto productDto, MultipartFile image) throws IOException {
        log.info("Starting product creation for: {}", productDto.getName());

        var product = ProductMapper.INSTANCE.toEntity(productDto);

        String imagePath = null;

        if (image != null && !image.isEmpty()) {
            log.info("Saving image for product: {}", productDto.getName());
            imagePath = saveImage(image);
        } else {
            log.warn("No image provided or image is empty");
        }

        product.setImagePath(imagePath);

        productRepository.save(product);
        log.info("Product saved successfully with ID: {}", product.getId());
    }

    private String saveImage(MultipartFile image) throws IOException {
        log.info("Start saving image...");

        String uploadDir = System.getProperty("user.dir") + "/uploads/images/";
        log.debug("Upload directory: {}", uploadDir);

        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) {
            boolean created = uploadFolder.mkdirs();
            if (created) {
                log.info("Created upload directory: {}", uploadDir);
            } else {
                log.error("Failed to create upload directory: {}", uploadDir);
                throw new IOException("Could not create directory for uploads");
            }
        } else {
            log.info("Upload directory already exists: {}", uploadDir);
        }

        String originalFileName = image.getOriginalFilename();
        if (originalFileName.isEmpty()) {
            log.error("Uploaded image has no file name!");
            throw new IOException("Uploaded image has no file name");
        }

        String fileName = UUID.randomUUID() + "_" + originalFileName;
        File file = new File(uploadDir + fileName);
        log.debug("Saving image as: {}", file.getAbsolutePath());

        image.transferTo(file);
        log.info("Image saved successfully: {}", file.getAbsolutePath());

        return "/images/" + fileName;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        log.info("Fetching all products from the database");
        List<Product> products = productRepository.findAll();
        log.info("Fetched {} products", products.size());
        return products;
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        log.info("Deleting product with ID: {}", productId);
        if (!productRepository.existsById(productId)) {
            log.warn("Product with ID {} not found, nothing to delete", productId);
            return;
        }
        productRepository.deleteById(productId);
        log.info("Product with ID {} deleted successfully", productId);
    }
}
