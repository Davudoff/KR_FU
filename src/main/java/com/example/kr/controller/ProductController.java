package com.example.kr.controller;

import com.example.kr.model.Product;
import com.example.kr.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Отображение списка всех товаров
    @GetMapping
    public String getAllProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("productCount", products.size());
        return "product-list"; // product-list.html
    }

    // Показать форму добавления нового товара
    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "add-product"; // add-product.html
    }

    // Сохранение нового товара
    @PostMapping("/add")
    public String saveProduct(@ModelAttribute Product product) {
        productService.saveProduct(product);
        return "redirect:/products";
    }

    // Показать форму редактирования товара
    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "edit-product"; // edit-product.html
    }

    // Сохранение изменений после редактирования товара
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product) {
        productService.updateProduct(id, product);
        return "redirect:/products";
    }

    // Удаление товара
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }

    // Поиск товаров по типу
    @GetMapping("/search")
    public String searchByProductType(@RequestParam(required = false) String productType, Model model) {
        List<Product> products = productService.searchByProductType(productType);
        model.addAttribute("products", products);
        return "product-list"; // product-list.html с результатами поиска
    }

    // Сортировка товаров по выбранному полю
    @GetMapping("/sort")
    public String sortProducts(@RequestParam String field, Model model) {
        List<Product> sortedProducts = productService.sortProductsByField(field);
        model.addAttribute("products", sortedProducts);
        return "product-list"; // Возвращает ту же страницу с отсортированными данными
    }

    // Показать страницу гистограммы
    @GetMapping("/histogram")
    public String showHistogramPage(Model model) {
        List<Map<String, Object>> chartData = productService.getHistogramData();
        model.addAttribute("chartData", chartData);
        return "histogram"; // histogram.html
    }
}
