package com.ultra.ecommerce.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;
@Data
public class CreateOrderRequest {
    @NotNull(message = "User ID is required")
    private Long userId; // ID of the user placing the order
    private Map<Long, Integer> productQuantities; // Map of product IDs to quantities


}

