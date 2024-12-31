package com.ultra.ecommerce.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductUpdateDTO {
    @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters")
    private String name;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private Double price;

    private String description;
    private Integer quantity;
}
