package com.alten.producttrialmasterbackend.service;

import com.alten.producttrialmasterbackend.dto.ProductDto;
import com.alten.producttrialmasterbackend.dto.utils.ProductDtoMapper;
import com.alten.producttrialmasterbackend.entities.Product;
import com.alten.producttrialmasterbackend.exception.ProductNotFoundException;
import com.alten.producttrialmasterbackend.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceUTest {

    @InjectMocks
    ProductService productService;
    @Mock
    ProductRepository productRepository;
    @Mock
    ProductDtoMapper mapper;

    @Test
    void shouldCreateNewProduct() {

        doCallRealMethod().when(mapper).mapToProduct(any(ProductDto.class));
        ProductDto productDto = new ProductDto();
        Product product = buildLightProduct(productDto);
        when(productRepository.save(product)).thenReturn(product);
        Product createdProduct = productService.createNewProduct(productDto);

        assertThat(createdProduct).isEqualTo(product);
        verify(productRepository).save(product);
    }

    @Test
    void shouldUpdateExistingProduct() throws ProductNotFoundException {
        doCallRealMethod().when(mapper).mapToProduct(any(ProductDto.class));
        ProductDto productDto = buildLightProductDto();
        Product product = buildLightProduct(productDto);

        when(productRepository.existsById(1L)).thenReturn(Boolean.TRUE);
        when(productRepository.save(product)).thenReturn(product);

        Product updatedProduct = productService.updateExistingProduct(productDto);

        assertThat(updatedProduct).isEqualTo(product);
    }

    @Test()
    void shouldThrowExceptionWhenProductNotFound() {
        ProductDto productDto = new ProductDto();
        productDto.setId(1L);

        when(productRepository.existsById(1L)).thenReturn(Boolean.FALSE);

        assertThatThrownBy(() -> productService.updateExistingProduct(productDto))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product not found for id : 1");
    }

    @Test
    void shouldRetrieveAllProducts() {
        List<Product> products = Arrays.asList(buildLightProduct(1L, "Iphone", 700.0), buildLightProduct(2L, "PC DELL", 1000.0));
        when(productRepository.findAll()).thenReturn(products);

        List<Product> expectedProducts = productService.retrieveProducts();

        assertThat(expectedProducts).hasSize(2);
    }

    @Test
    void shouldReturnEmptyList_whenThereAreNoProducts() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        List<Product> expectedProducts = productService.retrieveProducts();

        assertThat(expectedProducts).hasSize(0);
    }

    @Test
    void shouldRetrieveExistingProductById() throws ProductNotFoundException {
        Product product = buildLightProduct(1L, "Macbook Air", 1200.50);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product expectedProduct = productService.retrieveProductById(1L);

        assertThat(expectedProduct).isEqualTo(product);
    }

    @Test
    void shouldThrowExceptionWhenProductNotExitForId() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.retrieveProductById(1L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product not found for id : 1");
    }

    @Test
    void shouldDeleteProduct() {
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProductById(1L);

        verify(productRepository).deleteById(1L);
    }

    Product buildLightProduct(Long id, String name, Double price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    Product buildLightProduct(ProductDto productDto) {
        return mapper.mapToProduct(productDto);
    }

    ProductDto buildLightProductDto() {
        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("PC DELL");
        productDto.setPrice(2099.9);
        return productDto;
    }

}