package com.iyzico.challenge.repository;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.entity.Purchase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author erenadiguzel
 */

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class PurchaseRepositoryTest  {

    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private ProductRepository productRepository;

    private Product product;
    private Purchase purchase;

    @BeforeEach
    public void init() {
        product = new Product();
        product.setName("test-product");
        product.setDescription("Test product Description");
        product.setQuantity(10);
        product.setPrice(BigDecimal.TEN);
        productRepository.save(product);

        purchase = null;
    }

    @Test
    public void should_save_purchase() {
        Purchase purchase = new Purchase(product, 5);
        purchaseRepository.save(purchase);

        Optional<Purchase> purchaseById = purchaseRepository.findById(purchase.getId());

        assertAll(
                () -> assertTrue(purchaseById.isPresent()),
                () -> assertEquals(purchase.calculateTotalPrice(), purchaseById.get().getTotalPrice()),
                () -> assertNotNull(purchaseById.get().getProduct()),
                () -> assertEquals(product.getName(), purchaseById.get().getProduct().getName())
        );
    }

    @Test
    public void should_find_all_purchases() {
        Purchase purchase = new Purchase(product, 5);
        purchaseRepository.save(purchase);

        List<Purchase> purchases = purchaseRepository.findAll();

        assertAll(
                () -> assertEquals(1,purchases.size()),
                () -> assertThat(purchases).extracting("id")
                        .containsExactlyInAnyOrder(purchase.getId()),
                () -> assertThat(purchases).extracting("totalPrice")
                        .containsExactlyInAnyOrder(purchase.calculateTotalPrice())
        );
    }

    @Test
    public void should_find_by_id() {
        Purchase purchase = new Purchase(product, 5);
        purchaseRepository.save(purchase);

        Optional<Purchase> purchaseById = purchaseRepository.findById(purchase.getId());

        assertAll(
                () -> assertTrue(purchaseById.isPresent()),
                () -> assertEquals(purchase.getId(), purchaseById.get().getId())
        );
    }

    @Test
    public void should_delete_by_id() {
        Purchase purchase = new Purchase(product, 5);
        purchaseRepository.save(purchase);
        purchaseRepository.delete(purchase);

        Optional<Purchase> purchaseById = purchaseRepository.findById(purchase.getId());

        assertAll(
                () -> assertFalse(purchaseById.isPresent())
        );
    }
}