package com.example.store.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ProductService {

    String foo() {
        return "FOO!";
    }

}
