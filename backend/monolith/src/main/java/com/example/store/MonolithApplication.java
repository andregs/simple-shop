package com.example.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MonolithApplication {

    // TODO yes, conditional on properties seems a simpler solution for feature toggles
    // however, I have tested this only in junit -- does the angular app still work?
    // I have to test both strategies (by properties vs by dir)

    public static void main(String[] args) {
        SpringApplication.run(MonolithApplication.class, args);
    }

}
