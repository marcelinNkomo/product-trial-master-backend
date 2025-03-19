package com.alten.producttrialmasterbackend.controller.advice;

import com.alten.producttrialmasterbackend.exception.ProductNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * Gestion des erreurs metiers sur le controlleur ProductController
 */
@RestControllerAdvice
public class ProductExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> productNotFoundExceptionHandler(Exception cause) {
        return new ResponseEntity<>(cause.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }
}
