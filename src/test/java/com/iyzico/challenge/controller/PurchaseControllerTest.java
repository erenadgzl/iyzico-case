package com.iyzico.challenge.controller;

import com.iyzico.challenge.exception.OutOfStockException;
import com.iyzico.challenge.exception.ProductNotFoundException;
import com.iyzico.challenge.model.PurchaseRequestModel;
import com.iyzico.challenge.service.PurchaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author erenadiguzel
 */

@SpringBootTest
@ExtendWith(SpringExtension.class)
class PurchaseControllerTest {

    @InjectMocks
    private PurchaseController purchaseController;
    @Mock
    private PurchaseService purchaseService;

    private PurchaseRequestModel purchaseRequestModel;

    @BeforeEach
    void init() {
        purchaseRequestModel = new PurchaseRequestModel();
        purchaseRequestModel.setQuantity(1);
        purchaseRequestModel.setProductId(1L);
    }

    @Test
    void should_create_purchase() throws ProductNotFoundException, OutOfStockException {
        //Given
        when(purchaseService.createPurchase(purchaseRequestModel)).thenReturn(1L);

        //When
        ResponseEntity<Long> responseEntity = purchaseController.createPurchase(purchaseRequestModel);

        //Then
        assertAll(
                () -> assertEquals(201, responseEntity.getStatusCodeValue()),
                () -> assertNotNull(responseEntity.getBody()),
                () -> assertEquals(Long.valueOf(1), responseEntity.getBody())
        );
    }
}