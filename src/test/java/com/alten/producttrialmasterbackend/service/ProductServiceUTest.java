package com.alten.producttrialmasterbackend.service;

import com.alten.producttrialmasterbackend.entities.Product;
import com.alten.producttrialmasterbackend.exception.ProductNotFoundException;
import com.alten.producttrialmasterbackend.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceUTest {

    @InjectMocks
    ProductService productService;
    @Mock
    ProductRepository productRepository;

    @Test
    void shouldCreateNewProduct() {
        Product product = new Product();
        when(productRepository.save(product)).thenReturn(product);
        Product createdProduct = productService.createNewProduct(product);

        assertThat(createdProduct).isEqualTo(product);
        verify(productRepository).save(product);
    }

    @Test
    void shouldUpdateExistingProduct() throws ProductNotFoundException {
        Product product = buildLightProduct(1L, "PC DELL", 2099.90);

        when(productRepository.existsById(1L)).thenReturn(Boolean.TRUE);
        when(productRepository.save(product)).thenReturn(product);

        Product updatedProduct = productService.updateExistingProduct(product);

        assertThat(updatedProduct).isEqualTo(product);
    }

    @Test()
    void shouldThrowExceptionWhenProductNotFound() {
        Product product = new Product();
        product.setId(1L);

        when(productRepository.existsById(1L)).thenReturn(Boolean.FALSE);

        assertThatThrownBy(() -> productService.updateExistingProduct(product))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product not found for id : 1");
    }

    @ParameterizedTest
    @MethodSource("productsProvider")
    void shouldRetrieveAllProducts(List<Product> products) {
        when(productRepository.findAll()).thenReturn(products);

        List<Product> expectedProducts = productService.retrieveProducts();

        assertThat(expectedProducts).hasSize(products.size());
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

    static Stream<Arguments> productsProvider() {
        return Stream.of(
                Arguments.of(Collections.emptyList()),
                Arguments.of(Arrays.asList(
                        buildLightProduct(1L, "Iphone", 700.0),
                        buildLightProduct(2L, "PC DELL", 1000.0)
                ))
        );
    }

    static Product buildLightProduct(Long id, String name, Double price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }

}