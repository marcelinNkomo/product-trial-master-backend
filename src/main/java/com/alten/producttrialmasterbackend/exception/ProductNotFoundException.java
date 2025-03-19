package com.alten.producttrialmasterbackend.exception;

public class ProductNotFoundException extends Exception {

    public ProductNotFoundException(Long id) {
        super("Product not found for id : %d".formatted(id));
    }
}
