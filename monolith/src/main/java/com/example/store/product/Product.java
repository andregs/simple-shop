package com.example.store.product;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@NoArgsConstructor
@Data
@Entity
public class Product {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String name;

    private BigDecimal price;

    private int currentAmount;

    public Product(String name, BigDecimal price, int currentAmount) {
        this.name = name;
        this.price = price;
        this.currentAmount = currentAmount;
    }

}
