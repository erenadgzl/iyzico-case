package com.iyzico.challenge.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author erenadiguzel
 */

@SpringBootTest
class PurchaseTest {

    private Product product;
    private Purchase purchase;

    @BeforeEach
    void init() {
        product = new Product();
        product.setName("test-product");
        product.setDescription("Test product Description");
        product.setQuantity(10);
        product.setPrice(BigDecimal.TEN);

        purchase = new Purchase(product, 5);
    }

    @Test
    void calculateTotalPrice() {
        assertAll(
                () -> assertEquals(BigDecimal.valueOf(50), purchase.calculateTotalPrice())
        );
    }
}