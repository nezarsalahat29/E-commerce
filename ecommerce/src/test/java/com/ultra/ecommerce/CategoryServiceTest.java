package com.ultra.ecommerce;

import com.ultra.ecommerce.dtos.CategoryCreateDto;
import com.ultra.ecommerce.entity.Category;
import com.ultra.ecommerce.repository.CategoryRepository;
import com.ultra.ecommerce.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    private CategoryRepository mockRepository;
    private CategoryService categoryService;

    @BeforeEach
    public void setUp() {
        mockRepository = mock(CategoryRepository.class);
        categoryService = new CategoryService(mockRepository);
    }

    @Test
    public void test_add_category_with_valid_name() {
        // Arrange
        CategoryCreateDto dto = new CategoryCreateDto();
        dto.setName("Electronics");

        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName("Electronics");

        when(mockRepository.save(any(Category.class))).thenReturn(savedCategory);

        // Act
        Category result = categoryService.addCategory(dto);

        // Assert
        assertNotNull(result);
        assertEquals("Electronics", result.getName());
        verify(mockRepository).save(any(Category.class));
    }

    @Test
    public void test_retrieve_existing_category_by_id() {
        // Arrange
        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        when(mockRepository.findById(1L)).thenReturn(Optional.of(category));

        // Act
        Category result = categoryService.getCategory(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Electronics", result.getName());
        assertEquals(1L, result.getId());
    }

    @Test
    public void test_update_category_name_with_valid_data() {
        // Arrange
        Category existingCategory = new Category();
        existingCategory.setId(1L);
        existingCategory.setName("Electronics");

        CategoryCreateDto updateDto = new CategoryCreateDto();
        updateDto.setName("Home Appliances");

        when(mockRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(mockRepository.save(existingCategory)).thenReturn(existingCategory);

        // Act
        Category updatedCategory = categoryService.updateCategory(1L, updateDto);

        // Assert
        assertNotNull(updatedCategory);
        assertEquals("Home Appliances", updatedCategory.getName());
    }
}