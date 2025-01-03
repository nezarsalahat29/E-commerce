package com.ultra.ecommerce.service;

import com.ultra.ecommerce.dtos.CreateOrderRequest;
import com.ultra.ecommerce.entity.Order;
import com.ultra.ecommerce.entity.Product;
import com.ultra.ecommerce.entity.User;
import com.ultra.ecommerce.enums.OrderStatus;
import com.ultra.ecommerce.repository.OrderRepository;
import com.ultra.ecommerce.repository.ProductRepository;
import com.ultra.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }
    @Transactional
    public Order createOrder(CreateOrderRequest createOrderRequest) {
        Long userId = createOrderRequest.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Product> products = new ArrayList<>();
        createOrderRequest.getProductQuantities().forEach((productId, quantity) -> {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            // TODO: Check if the product has enough stock
            for (int i = 0; i < quantity; i++) {
                product.reduceStock(1);
                products.add(product);
            }
        });
        Order order = new Order();
        order.setUser(user);
        order.setProducts(products);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        return orderRepository.save(order);
    }
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
    
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order updateOrderStatus(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.COMPLETED);
        return orderRepository.save(order);
    }

    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.CANCELED);
         return orderRepository.save(order);
    }


}
