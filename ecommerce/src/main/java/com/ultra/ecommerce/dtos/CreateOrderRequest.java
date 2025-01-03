package com.ultra.ecommerce.dtos;

import lombok.Data;

import java.util.Map;
@Data
public class CreateOrderRequest {

    private Long userId; // ID of the user placing the order
    private Map<Long, Integer> productQuantities; // Map of product IDs to quantities


}

