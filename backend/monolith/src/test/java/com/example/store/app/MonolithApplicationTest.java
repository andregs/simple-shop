package com.example.store.app;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.store.product.ProductService;
import com.example.store.user.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MonolithApplicationTest {
    
    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Test
    void contextLoads() {
        assertThat(productService).isNotNull();
        assertThat(userService).isNotNull();
    }

}