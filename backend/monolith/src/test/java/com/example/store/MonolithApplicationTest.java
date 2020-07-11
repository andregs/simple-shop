package com.example.store;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.store.product.ProductService;
import com.example.store.user.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MonolithApplicationTest {
    
    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    UserController userController;

    @Autowired
    UserRepository userRepository;

    @Test
    void contextLoads() {
        assertThat(productService).isNotNull();
        assertThat(userService).isNotNull();
        assertThat(userController).isNotNull();
        assertThat(userRepository).isNotNull();
    }

}
