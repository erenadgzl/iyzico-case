package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.entity.Purchase;
import com.iyzico.challenge.exception.OutOfStockException;
import com.iyzico.challenge.exception.ProductNotFoundException;
import com.iyzico.challenge.model.PurchaseRequestModel;
import com.iyzico.challenge.repository.PurchaseRepository;
import com.iyzico.challenge.service.implementation.DefaultPurchaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author erenadiguzel
 */

@ExtendWith(SpringExtension.class)
class PurchaseServiceTest {

    @InjectMocks
    private DefaultPurchaseService purchaseService;
    @Mock
    private PurchaseRepository purchaseRepository;
    @Mock
    private ProductService productService;
    @Mock
    private IyzicoPaymentService iyzicoPaymentService;

    private Purchase purchase;
    private Product product;
    private PurchaseRequestModel purchaseRequestModel;

    @BeforeEach
    void init() {
        product = new Product();
        product.setId(1L);
        product.setName("test-product");
        product.setDescription("Test product Description");
        product.setQuantity(10);
        product.setPrice(BigDecimal.TEN);

        purchaseRequestModel = new PurchaseRequestModel();
        purchaseRequestModel.setProductId(1L);
        purchaseRequestModel.setQuantity(1);

        purchase = new Purchase(product, 1);
        purchase.setId(1L);
    }

    @Test
    void should_create_purchase() throws ProductNotFoundException, OutOfStockException {
        //Given
        when(productService.findById(any())).thenReturn(product);
        when(purchaseRepository.save(any())).thenReturn(purchase);

        //When
        Long purchaseId = purchaseService.createPurchase(purchaseRequestModel);

        //Then
        assertAll(
                () -> assertNotNull(purchaseId),
                () -> assertEquals(Long.valueOf(1), purchaseId),
                () -> assertEquals(product.getId(), purchase.getProduct().getId()),
                () -> assertEquals(product.getName(), purchase.getProduct().getName())
        );
    }

    @Test
    void should_throw_out_of_stock_exception_when_create_purchase() throws ProductNotFoundException {
        //Given
        when(productService.findById(any())).thenReturn(product);
        when(purchaseRepository.save(any())).thenReturn(purchase);
        purchaseRequestModel.setQuantity(10000);

        //When
        OutOfStockException thrown = assertThrows(
                OutOfStockException.class,
                () -> purchaseService.createPurchase(purchaseRequestModel),
                "Expected OutOfStockException to throw, but it didn't"
        );

        //Then
        assertTrue(thrown.getMessage().contains("This product is out of stock!"));
    }

    @Test
    void should_throw_out_of_stock_exception_when_ten_purchasing_in_same_time() throws ProductNotFoundException, OutOfStockException {
        //Given
        when(productService.findById(any())).thenReturn(product);
        when(purchaseRepository.save(any())).thenReturn(purchase);
        final int[] outOfStockExpCounter = {0};
        int PURCHASE_COUNT = 20;
        int productQuantity = product.getQuantity();

        //When
        List<CompletableFuture> futures = new ArrayList<>();
        for (int i = 0; i < PURCHASE_COUNT; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    purchaseService.createPurchase(purchaseRequestModel);
                } catch (ProductNotFoundException e) {
                    e.printStackTrace();
                } catch (OutOfStockException e) {
                    outOfStockExpCounter[0]++;
                }
            });
            futures.add(future);
        }
        futures.stream().forEach(f -> CompletableFuture.allOf(f).join());

        //Then
        assertEquals(Integer.valueOf(0), product.getQuantity());
        assertEquals(PURCHASE_COUNT - productQuantity, outOfStockExpCounter[0]);
    }


}