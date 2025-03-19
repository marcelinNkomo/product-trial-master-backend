package com.alten.producttrialmasterbackend.service;

import com.alten.producttrialmasterbackend.entities.Product;
import com.alten.producttrialmasterbackend.exception.ProductNotFoundException;
import com.alten.producttrialmasterbackend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product createNewProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateExistingProduct(Product updatedProduct) throws ProductNotFoundException {
        Long id = updatedProduct.getId();
        if (!productRepository.existsById(id))
            throw new ProductNotFoundException(id);

        return productRepository.save(updatedProduct);
    }

    public List<Product> retrieveProducts() {
        return productRepository.findAll();
    }

    public Product retrieveProductById(Long id) throws ProductNotFoundException {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }
}
