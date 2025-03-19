package com.example.online_shop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ProductOutOfStockException extends RuntimeException {

    public ProductOutOfStockException(String productName, int availableQuantity, int requestedQuantity) {
        super(String.format(
                "Product '%s' is out of stock. Available: %d, Requested: %d",
                productName, availableQuantity, requestedQuantity
        ));
    }
}
