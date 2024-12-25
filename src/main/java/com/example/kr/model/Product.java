package com.example.kr.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productType;
    private String productGroup;

    private LocalDate importDate;
    private LocalDate exportDate;

    private String driverName;

    // Новое поле для цены
    @Column(nullable = false) // Поле обязательное
    private BigDecimal price;
}
