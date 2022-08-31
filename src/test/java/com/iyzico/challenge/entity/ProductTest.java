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
class ProductTest {

    private Product product;

    @BeforeEach
    void init() {
        product = new Product();
        product.setName("test-product");
        product.setDescription("Test product Description");
        product.setQuantity(10);
        product.setPrice(BigDecimal.TEN);
    }

    @Test
    void should_is_stock_available_success() {
        assertAll(
                () -> assertTrue(product.isStockAvailable(10)),
                () -> assertTrue(product.isStockAvailable(5)),
                () -> assertFalse(product.isStockAvailable(11))
        );
    }

    @Test
    void should_reduce_stock() {
        product.reduceStock(5);

        assertAll(
                () -> assertEquals(Integer.valueOf(5), product.getQuantity()),
                () -> assertTrue(product.isStockAvailable(5)),
                () -> assertFalse(product.isStockAvailable(6))
        );
    }
}