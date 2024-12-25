package com.example.kr.service;

import com.example.kr.model.Product;
import com.example.kr.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Добавить новый товар
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    // Удалить товар по ID
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Обновить существующий товар
    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар с ID " + id + " не найден"));

        product.setProductType(productDetails.getProductType());
        product.setProductGroup(productDetails.getProductGroup());
        product.setImportDate(productDetails.getImportDate());
        product.setExportDate(productDetails.getExportDate());
        product.setDriverName(productDetails.getDriverName());
        product.setPrice(productDetails.getPrice());

        return productRepository.save(product);
    }

    // Получить товар по ID
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар с ID " + id + " не найден"));
    }

    // Получить все товары
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Сортировка товаров по заданному полю и порядку
    public List<Product> getAllProductsSorted(String sortField, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        return productRepository.findAll(sort);
    }

    // Фильтрация товаров по дате ввоза
    public List<Product> filterProductsByImportDate(LocalDate date) {
        return productRepository.findByImportDateAfter(date);
    }

    // Поиск товаров по различным параметрам (тип, группа, цена)
    public List<Product> searchProducts(String productType, String productGroup, BigDecimal price) {
        if (productType != null && !productType.isEmpty() && productGroup != null && !productGroup.isEmpty() && price != null) {
            return productRepository.findByProductTypeContainingIgnoreCaseAndProductGroupContainingIgnoreCaseAndPrice(
                    productType, productGroup, price);
        } else if (productType != null && !productType.isEmpty() && productGroup != null && !productGroup.isEmpty()) {
            return productRepository.findByProductTypeContainingIgnoreCaseAndProductGroupContainingIgnoreCase(
                    productType, productGroup);
        } else if (productType != null && !productType.isEmpty()) {
            return productRepository.findByProductTypeContainingIgnoreCase(productType);
        } else if (productGroup != null && !productGroup.isEmpty()) {
            return productRepository.findByProductGroupContainingIgnoreCase(productGroup);
        } else if (price != null) {
            return productRepository.findByPrice(price);
        } else {
            return productRepository.findAll();
        }
    }

    // Получение данных для гистограммы
    public List<Map<String, Object>> getHistogramData() {
        return productRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Product::getImportDate,
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .map(entry -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("date", entry.getKey().toString());
                    data.put("count", entry.getValue());
                    return data;
                })
                .sorted(Comparator.comparing(entry -> LocalDate.parse(entry.get("date").toString())))
                .collect(Collectors.toList());
    }

    // Сохранение товара
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    // Поиск товаров по типу
    public List<Product> searchByProductType(String productType) {
        if (productType != null && !productType.isEmpty()) {
            return productRepository.findByProductTypeContainingIgnoreCase(productType);
        } else {
            return productRepository.findAll();
        }
    }

    // Сортировка товаров по заданному полю
    public List<Product> sortProductsByField(String sortField) {
        List<Product> products = productRepository.findAll();

        switch (sortField) {
            case "id":
                products.sort(Comparator.comparing(Product::getId));
                break;
            case "productType":
                products.sort(Comparator.comparing(Product::getProductType, String.CASE_INSENSITIVE_ORDER));
                break;
            case "productGroup":
                products.sort(Comparator.comparing(Product::getProductGroup, String.CASE_INSENSITIVE_ORDER));
                break;
            case "importDate":
                products.sort(Comparator.comparing(Product::getImportDate));
                break;
            case "price":
                products.sort(Comparator.comparing(Product::getPrice));
                break;
            default:
                throw new IllegalArgumentException("Некорректное поле для сортировки: " + sortField);
        }

        return products;
    }
}
