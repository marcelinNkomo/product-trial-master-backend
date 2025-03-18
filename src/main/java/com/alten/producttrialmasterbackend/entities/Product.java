package com.alten.producttrialmasterbackend.entities;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    private String description;
    private String image;
    private String category;
    private Double price;
    private Double quantity;
    @Column(name = "internal-reference")
    private String internalReference;
    @Column(name = "shell-id")
    private Long shellId;
    @Column(name = "inventory-status")
    private InventoryStatus inventoryStatus;
    private Double rating;
    @Column(name = "create-at")
    @CreationTimestamp(source = SourceType.DB)
    private Instant createdAt;
    @UpdateTimestamp(source = SourceType.DB)
    @Column(name = "update-at")
    private Instant updatedAt;
}
