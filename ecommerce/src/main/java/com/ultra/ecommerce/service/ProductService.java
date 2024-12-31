package com.ultra.ecommerce.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ultra.ecommerce.dtos.ProductCreateDTO;
import com.ultra.ecommerce.dtos.ProductUpdateDTO;
import com.ultra.ecommerce.entity.Product;
import com.ultra.ecommerce.entity.User;
import com.ultra.ecommerce.repository.ProductRepository;
import com.ultra.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;


    public ProductService(ProductRepository productRepository, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    public Product addProduct(ProductCreateDTO productCreateDTO) {
        return productRepository.save(objectMapper.convertValue(productCreateDTO, Product.class));
    }
    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }
    public Product updateProduct(ProductUpdateDTO productUpdateDTO) {
        return productRepository.save(objectMapper.convertValue(productUpdateDTO, Product.class));
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