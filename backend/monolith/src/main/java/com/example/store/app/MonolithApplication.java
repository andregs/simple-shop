package com.example.store.app;

import com.example.store.product.ProductConfiguration;
import com.example.store.user.UserConfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({UserConfiguration.class, ProductConfiguration.class})
public class MonolithApplication {

    // TODO move this to 'app' package to disable full component scan
    // then probably I can make these work:
    // https://reflectoring.io/testing-verticals-and-layers-spring-boot/
    // 
    // RESULT: yes, moving the files was enough to make the modules work independently
    // however, I have tested this only in junit -- does the angular app still work?
    // besides that, I should probably stick to default (main class in root folder), and
    // then toggle my feature modules with @ConditionalOn. see https://reflectoring.io/spring-boot-modules/

    public static void main(String[] args) {
        SpringApplication.run(MonolithApplication.class, args);
    }

}
