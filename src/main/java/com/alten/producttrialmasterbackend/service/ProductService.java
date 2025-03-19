package com.alten.producttrialmasterbackend.service;

import com.alten.producttrialmasterbackend.dto.ProductDto;
import com.alten.producttrialmasterbackend.dto.utils.ProductDtoMapper;
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
    private final ProductDtoMapper mapper;

    public Product createNewProduct(ProductDto productDto) {
        return productRepository.save(mapper.mapToProduct(productDto));
    }

    public Product updateExistingProduct(ProductDto updatedProductDto) throws ProductNotFoundException {
        Long id = updatedProductDto.getId();
        if (!productRepository.existsById(id))
            throw new ProductNotFoundException(id);

        return productRepository.save(mapper.mapToProduct(updatedProductDto));
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
