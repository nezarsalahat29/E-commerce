package com.ultra.ecommerce;

import com.ultra.ecommerce.entity.User;
import com.ultra.ecommerce.repository.UserRepository;
import com.ultra.ecommerce.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private UserRepository mockRepo;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        mockRepo = mock(UserRepository.class);
        userService = new UserService(mockRepo);
    }

    @Test
    public void test_all_users_returns_multiple_users() {
        List<User> mockUsers = Arrays.asList(new User(), new User(), new User());
        when(mockRepo.findAll()).thenReturn(mockUsers);

        List<User> result = userService.allUsers();

        assertEquals(3, result.size());
        verify(mockRepo).findAll();
    }

    @Test
    public void test_all_users_handles_large_dataset() {
        List<User> largeUserList = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            largeUserList.add(new User());
        }
        when(mockRepo.findAll()).thenReturn(largeUserList);

        long startTime = System.currentTimeMillis();
        List<User> result = userService.allUsers();
        long endTime = System.currentTimeMillis();

        assertTrue((endTime - startTime) < 1000);
        assertEquals(10000, result.size());
        verify(mockRepo).findAll();
    }

    @Test
    public void test_all_users_returns_empty_list_when_no_users_exist() {
        when(mockRepo.findAll()).thenReturn(new ArrayList<>());

        List<User> result = userService.allUsers();

        assertTrue(result.isEmpty());
        verify(mockRepo).findAll();
    }

    @Test
    public void test_all_users_handles_database_timeout() {
        when(mockRepo.findAll()).thenThrow(new RuntimeException("Database connection timeout"));

        RuntimeException exception = assertThrows(RuntimeException.class, userService::allUsers);

        assertEquals("Database connection timeout", exception.getMessage());
        verify(mockRepo).findAll();
    }

    @Test
    public void test_all_users_converts_iterable_to_arraylist() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        User user2 = new User();
        user2.setEmail("user2@example.com");

        List<User> mockUsers = List.of(user1, user2);
        when(mockRepo.findAll()).thenReturn(mockUsers);

        List<User> result = userService.allUsers();

        assertEquals(2, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));
    }

    @Test
    public void test_constructor_initializes_user_repository() {
        assertNotNull(userService);
    }

    @Test
    public void test_all_users_handles_concurrent_access() throws InterruptedException {
        List<User> mockUsers = List.of(new User(), new User());
        when(mockRepo.findAll()).thenReturn(mockUsers);

        Runnable task = () -> {
            List<User> result = userService.allUsers();
            assertEquals(2, result.size());
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }
}