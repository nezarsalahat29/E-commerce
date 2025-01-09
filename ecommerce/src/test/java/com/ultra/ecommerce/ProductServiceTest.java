package com.ultra.ecommerce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ultra.ecommerce.dtos.ProductCreateDTO;
import com.ultra.ecommerce.dtos.ProductUpdateDTO;
import com.ultra.ecommerce.entity.Category;
import com.ultra.ecommerce.entity.Product;
import com.ultra.ecommerce.repository.CategoryRepository;
import com.ultra.ecommerce.repository.ProductRepository;
import com.ultra.ecommerce.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTest {

    private ProductRepository productRepository;
    private ObjectMapper objectMapper;
    private CategoryRepository categoryRepository;
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        productRepository = mock(ProductRepository.class);
        objectMapper = mock(ObjectMapper.class);
        categoryRepository = mock(CategoryRepository.class);
        productService = new ProductService(productRepository, objectMapper, categoryRepository);
    }

    @Test
    public void test_add_product_with_valid_dto_and_existing_category() {
        // Arrange
        ProductCreateDTO dto = new ProductCreateDTO();
        dto.setName("Test Product");
        dto.setDescription("Test Description");
        dto.setPrice(99.99);
        dto.setQuantity(10);
        dto.setCategoryId(1L);

        Category category = new Category();
        category.setId(1L);
        category.setName("Test Category");

        Product expectedProduct = new Product();
        expectedProduct.setName("Test Product");
        expectedProduct.setDescription("Test Description");
        expectedProduct.setPrice(99.99);
        expectedProduct.setQuantity(10);
        expectedProduct.setCategory(category);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(objectMapper.convertValue(dto, Product.class)).thenReturn(expectedProduct);
        when(productRepository.save(any(Product.class))).thenReturn(expectedProduct);

        // Act
        Product result = productService.addProduct(dto);

        // Assert
        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        assertEquals("Test Description", result.getDescription());
        assertEquals(99.99, result.getPrice());
        assertEquals(10, result.getQuantity());
        assertEquals(category, result.getCategory());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    public void test_add_product_with_nonexistent_category() {
        // Arrange
        ProductCreateDTO dto = new ProductCreateDTO();
        dto.setName("Test Product");
        dto.setDescription("Test Description");
        dto.setPrice(99.99);
        dto.setQuantity(10);
        dto.setCategoryId(999L);

        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.addProduct(dto);
        });

        assertEquals("Category not found", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void test_retrieve_product_by_valid_id() {
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Product retrievedProduct = productService.getProductById(productId);

        assertNotNull(retrievedProduct);
        assertEquals(productId, retrievedProduct.getId());
    }

    @Test
    public void test_update_product_with_valid_dto() {
        Long productId = 1L;
        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Old Name");
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        ProductUpdateDTO updateDTO = new ProductUpdateDTO();
        updateDTO.setName("New Name");

        // Mock the save method to return the updated product
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product updatedProduct = productService.updateProduct(productId, updateDTO);

        assertNotNull(updatedProduct, "Updated product should not be null");
        assertEquals("New Name", updatedProduct.getName(), "Product name should be updated to 'New Name'");
    }

    @Test
    public void test_delete_product_by_id() {
        Long productId = 1L;

        doNothing().when(productRepository).deleteById(productId);

        productService.deleteProduct(productId);

        verify(productRepository, times(1)).deleteById(productId);
    }
}