package com.ultra.ecommerce.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ultra.ecommerce.dtos.ProductCreateDTO;
import com.ultra.ecommerce.dtos.ProductUpdateDTO;
import com.ultra.ecommerce.entity.Category;
import com.ultra.ecommerce.entity.Product;
import com.ultra.ecommerce.repository.CategoryRepository;
import com.ultra.ecommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Transactional
    @CachePut(value = "products", key = "#id")
    public Product updateProduct(Long id, ProductUpdateDTO productUpdateDTO) {
        // Fetch the product
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Update fields if they are provided in DTO
        if (productUpdateDTO.getCategoryId() != null) {
            updateCategory(product, productUpdateDTO.getCategoryId());
        }
        if (productUpdateDTO.getName() != null) {
            product.setName(productUpdateDTO.getName());
        }
        if (productUpdateDTO.getPrice() != null) {
            product.setPrice(productUpdateDTO.getPrice());
        }
        if (productUpdateDTO.getDescription() != null) {
            product.setDescription(productUpdateDTO.getDescription());
        }

        // Save and return updated product
        return productRepository.save(product);
    }

    private void updateCategory(Product product, Long categoryId) {
        if (!categoryId.equals(product.getCategory().getId())) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }
    }

    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Cacheable(value = "products")
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        productRepository.findAll().forEach(products::add);
        return products;
    }


}