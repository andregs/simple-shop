package com.example.store.product;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@ConditionalOnProperty(prefix = "app.product", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ProductService {

    String foo() {
        return "FOO!";
    }

}
