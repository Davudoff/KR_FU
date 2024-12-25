package com.example.kr.repository;

import com.example.kr.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Фильтрация по дате ввоза
    List<Product> findByImportDateAfter(LocalDate date);

    // Поиск по типу и группе
    List<Product> findByProductTypeContainingIgnoreCaseAndProductGroupContainingIgnoreCase(String productType, String productGroup);

    // Поиск по типу, группе и цене
    List<Product> findByProductTypeContainingIgnoreCaseAndProductGroupContainingIgnoreCaseAndPrice(String productType, String productGroup, BigDecimal price);

    // Поиск по цене
    List<Product> findByPrice(BigDecimal price);

    // Поиск по типу товара (игнорируя регистр букв)
    List<Product> findByProductTypeContainingIgnoreCase(String productType);

    // Поиск по группе товара (игнорируя регистр)
    List<Product> findByProductGroupContainingIgnoreCase(String productGroup);

    List<Product> findAllByOrderByPriceAsc();
    List<Product> findAllByOrderByPriceDesc();
    List<Product> findAllByOrderByImportDateAsc();
    List<Product> findAllByOrderByImportDateDesc();


    // Группировка товаров по дате вывоза
    @Query("SELECT p.exportDate, COUNT(p) FROM Product p GROUP BY p.exportDate")
    List<Object[]> countProductsByExportDate();
}
