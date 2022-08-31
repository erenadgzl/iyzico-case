package com.iyzico.challenge.repository;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.entity.Purchase;
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
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    public void init() {
        product = new Product();
        product.setName("test-product");
        product.setDescription("Test product Description");
        product.setQuantity(10);
        product.setPrice(BigDecimal.TEN);
    }

    @Test
    void should_save_product(){
        productRepository.save(product);

        Optional<Product> productOptional = productRepository.findById(product.getId());

        assertAll(
                () -> assertTrue(productOptional.isPresent()),
                () -> assertEquals(product.getName(), productOptional.get().getName()),
                () -> assertEquals(product.getDescription(), productOptional.get().getDescription()),
                () -> assertEquals(product.getQuantity(), productOptional.get().getQuantity()),
                () -> assertEquals(0,product.getPrice().compareTo( productOptional.get().getPrice()))
        );
    }

    @Test
    void should_find_by_name() {
        productRepository.save(product);

        Optional<Product> productOptional = productRepository.findByName(product.getName());

        assertAll(
                () -> assertTrue(productOptional.isPresent()),
                () -> assertEquals(product.getName(), productOptional.get().getName()),
                () -> assertEquals(product.getDescription(), productOptional.get().getDescription()),
                () -> assertEquals(product.getQuantity(), productOptional.get().getQuantity()),
                () -> assertEquals(0,product.getPrice().compareTo(productOptional.get().getPrice()) )
        );
    }

    @Test
    void should_find_all() {
        productRepository.save(product);

        List<Product> products = productRepository.findAll();

        assertAll(
                () -> assertEquals(1,products.size()),
                () -> assertThat(products).extracting("id")
                        .containsExactlyInAnyOrder(product.getId()),
                () -> assertThat(products).extracting("name")
                        .containsExactlyInAnyOrder(product.getName())
        );
    }

    @Test
    public void should_delete_by_id() {
        productRepository.save(product);
        productRepository.delete(product);

        Optional<Product> productOptional = productRepository.findById(product.getId());

        assertAll(
                () -> assertFalse(productOptional.isPresent())
        );
    }
}