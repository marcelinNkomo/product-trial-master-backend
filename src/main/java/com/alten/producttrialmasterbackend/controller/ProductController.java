package com.alten.producttrialmasterbackend.controller;

import com.alten.producttrialmasterbackend.dto.ProductDto;
import com.alten.producttrialmasterbackend.entities.Product;
import com.alten.producttrialmasterbackend.exception.ProductNotFoundException;
import com.alten.producttrialmasterbackend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/products")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Endpoint de creation de produit
     *
     * @param productDto
     * @return
     */
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody ProductDto productDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createNewProduct(productDto));
    }

    /**
     * Endpoint qui retourne la liste des produits
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Product>> retrieve() {
        return ResponseEntity.ok(productService.retrieveProducts());
    }

    /**
     * Endpoint qui retourne un produit donnée
     *
     * @param id
     * @return
     * @throws ProductNotFoundException
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> retrieveById(@PathVariable Long id) throws ProductNotFoundException {
        return ResponseEntity.ok(productService.retrieveProductById(id));
    }

    /**
     * @param productDto
     * @param id
     * @return
     * @throws ProductNotFoundException
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Product> update(@RequestBody ProductDto productDto, @PathVariable Long id)
            throws ProductNotFoundException {
        productDto.setId(id); // en cas d'erreur sur l'id
        return ResponseEntity.ok(productService.updateExistingProduct(productDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        productService.deleteProductById(id);
        return new ResponseEntity<>("The product with id %d deleted successfully!".formatted(id), HttpStatus.OK);
    }

}
