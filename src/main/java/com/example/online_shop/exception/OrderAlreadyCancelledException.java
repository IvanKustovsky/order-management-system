package com.example.online_shop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class OrderAlreadyCancelledException extends RuntimeException {

    public OrderAlreadyCancelledException() {
        super("Order is already cancelled");
    }

    public OrderAlreadyCancelledException(String message) {
        super(message);
    }

    public OrderAlreadyCancelledException(String message, Throwable cause) {
        super(message, cause);
    }
}
