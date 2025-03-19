package com.alten.producttrialmasterbackend.dto.utils;

import com.alten.producttrialmasterbackend.dto.InventoryStatus;
import com.alten.producttrialmasterbackend.dto.ProductDto;
import com.alten.producttrialmasterbackend.entities.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductDtoMapper {

    public Product mapToProduct(ProductDto productDto) {
        Product product = new Product();
        if (productDto.getId() != null)
            product.setId(productDto.getId());
        if (productDto.getName() != null)
            product.setName(productDto.getName());
        if (productDto.getPrice() != null)
            product.setPrice(productDto.getPrice());
        if (productDto.getCategory() != null)
            product.setCategory(productDto.getCategory());
        if (productDto.getCode() != null)
            product.setCode(productDto.getCode());
        if (productDto.getImage() != null)
            product.setImage(productDto.getImage());
        if (productDto.getDescription() != null)
            product.setDescription(productDto.getDescription());
        if (productDto.getRating() != null)
            product.setRating(productDto.getRating());
        if (productDto.getInternalReference() != null)
            product.setInternalReference(productDto.getInternalReference());
        product.setInventoryStatus(productDto.getId() != null ? productDto.getInventoryStatus() : InventoryStatus.INSTOCK);
        if (productDto.getQuantity() != null)
            product.setQuantity(productDto.getQuantity());
        if (productDto.getShellId() != null)
            product.setShellId(productDto.getShellId());
        if (productDto.getCreatedAt() != null)
            product.setCreatedAt(productDto.getCreatedAt());
        if (productDto.getUpdatedAt() != null)
            product.setUpdatedAt(productDto.getUpdatedAt());

        return product;
    }
}
