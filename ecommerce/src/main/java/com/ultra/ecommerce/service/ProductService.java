package com.ultra.ecommerce.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ultra.ecommerce.dtos.ProductCreateDTO;
import com.ultra.ecommerce.dtos.ProductUpdateDTO;
import com.ultra.ecommerce.entity.Category;
import com.ultra.ecommerce.entity.Product;
import com.ultra.ecommerce.entity.User;
import com.ultra.ecommerce.repository.CategoryRepository;
import com.ultra.ecommerce.repository.ProductRepository;
import com.ultra.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, ObjectMapper objectMapper, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
        this.categoryRepository = categoryRepository;
    }

    public Product addProduct(ProductCreateDTO productCreateDTO) {
        Category category = categoryRepository.findById(productCreateDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Product product = objectMapper.convertValue(productCreateDTO, Product.class);
        product.setCategory(category);
        return productRepository.save(product);
    }
    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }
    public Product updateProduct(ProductUpdateDTO productUpdateDTO) {
        Product product = objectMapper.convertValue(productUpdateDTO, Product.class);
        if (productUpdateDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(productUpdateDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }
        return productRepository.save(product);
    }
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        productRepository.findAll().forEach(products::add);
        return products;
    }
}