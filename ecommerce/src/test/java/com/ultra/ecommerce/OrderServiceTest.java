package com.ultra.ecommerce;

import com.ultra.ecommerce.dtos.CreateOrderRequest;
import com.ultra.ecommerce.entity.Order;
import com.ultra.ecommerce.entity.Product;
import com.ultra.ecommerce.entity.User;
import com.ultra.ecommerce.enums.OrderStatus;
import com.ultra.ecommerce.repository.OrderRepository;
import com.ultra.ecommerce.repository.ProductRepository;
import com.ultra.ecommerce.repository.UserRepository;
import com.ultra.ecommerce.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    private UserRepository userRepository;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        productRepository = mock(ProductRepository.class);
        orderRepository = mock(OrderRepository.class);
        orderService = new OrderService(orderRepository, userRepository, productRepository);
    }

    @Test
    public void test_create_order_success() {
        // Arrange
        User user = new User();
        user.setId(1L);

        Product product = new Product();
        product.setId(1L);
        product.setQuantity(10);

        Map<Long, Integer> productQuantities = new HashMap<>();
        productQuantities.put(1L, 2);

        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserId(1L);
        request.setProductQuantities(productQuantities);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Order result = orderService.createOrder(request);

        // Assert
        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(OrderStatus.PENDING, result.getStatus());
        assertEquals(2, result.getProducts().size());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    public void test_create_order_with_invalid_user() {
        // Arrange
        Map<Long, Integer> productQuantities = new HashMap<>();
        productQuantities.put(1L, 1);

        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserId(999L);
        request.setProductQuantities(productQuantities);

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> orderService.createOrder(request));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    public void test_get_order_by_valid_id() {
        // Arrange
        Long orderId = 1L;
        Order expectedOrder = new Order();
        expectedOrder.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(expectedOrder));

        // Act
        Order actualOrder = orderService.getOrderById(orderId);

        // Assert
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    public void test_get_orders_by_user() {
        // Arrange
        Long userId = 1L;
        List<Order> expectedOrders = new ArrayList<>();
        expectedOrders.add(new Order());

        when(orderRepository.findByUserId(userId)).thenReturn(expectedOrders);

        // Act
        List<Order> actualOrders = orderService.getOrdersByUser(userId);

        // Assert
        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    public void test_update_order_status_to_completed() {
        // Arrange
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.PENDING);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        Order updatedOrder = orderService.updateOrderStatus(orderId);

        // Assert
        assertEquals(OrderStatus.COMPLETED, updatedOrder.getStatus());
    }
}