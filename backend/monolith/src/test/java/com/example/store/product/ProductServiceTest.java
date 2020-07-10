package com.example.store.product;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.store.user.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductServiceTest {
    
    @Autowired(required=false)
    ProductService productService;

    @Autowired(required=false)
    UserService userService;
    
    @Test
    void productTest() {
        assertThat(productService).isNotNull();
        assertThat(userService).isNull();
        assertThat(productService.foo()).isEqualTo("FOO!");
    }



}