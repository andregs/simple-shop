package com.example.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MonolithApplication {

    // TODO move this to 'app' package to disable full component scan
    // then probably I can make these work:
    // https://reflectoring.io/testing-verticals-and-layers-spring-boot/
    // https://reflectoring.io/spring-boot-modules/

    public static void main(String[] args) {
        SpringApplication.run(MonolithApplication.class, args);
    }

}
