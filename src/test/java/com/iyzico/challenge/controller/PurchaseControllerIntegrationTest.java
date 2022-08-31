package com.iyzico.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iyzico.challenge.exception.ProductNotFoundException;
import com.iyzico.challenge.model.ProductRequestModel;
import com.iyzico.challenge.model.PurchaseRequestModel;
import com.iyzico.challenge.repository.PurchaseRepository;
import com.iyzico.challenge.service.implementation.DefaultProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author erenadiguzel
 */

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Transactional
class PurchaseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private DefaultProductService productService;

    private ProductRequestModel productRequestModel;
    private Long purchaseId;
    private Long productId;


    @BeforeEach
    void init() {
        productRequestModel = new ProductRequestModel();
        productRequestModel.setName("test-product");
        productRequestModel.setDescription("Test product Description");
        productRequestModel.setQuantity(10);
        productRequestModel.setPrice(BigDecimal.TEN);
    }

    @AfterEach
    void clear() throws ProductNotFoundException {
        if (productId != null) {
            productService.delete(productId);
        }
        if (purchaseId != null) {
            purchaseRepository.deleteById(purchaseId);
        }

        productId = null;
        purchaseId = null;
    }


    @Test
    void should_create_purchase() throws Exception {
        //Given
        productId = productService.create(productRequestModel);
        PurchaseRequestModel purchaseRequestModel = new PurchaseRequestModel();
        purchaseRequestModel.setQuantity(1);
        purchaseRequestModel.setProductId(productId);
        String json = mapper.writeValueAsString(purchaseRequestModel);

        //When
        String response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/purchases/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        purchaseId = Long.valueOf(response);

        //Then
        assertAll(
                () -> assertNotNull(purchaseId)
        );
    }

    @Test
    void should_throw_out_of_stock_exception_when_create_purchase() throws Exception {
        //Given
        productId = productService.create(productRequestModel);
        PurchaseRequestModel purchaseRequestModel = new PurchaseRequestModel();
        purchaseRequestModel.setQuantity(56351);
        purchaseRequestModel.setProductId(productId);
        String json = mapper.writeValueAsString(purchaseRequestModel);

        //When
        String response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/purchases/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andReturn().getResponse().getContentAsString();

        //Then
        assertTrue(response.contains("This product is out of stock!"));
    }

    @Test
    void should_throw_product_not_found_exception_when_create_purchase() throws Exception {
        //Given
        PurchaseRequestModel purchaseRequestModel = new PurchaseRequestModel();
        purchaseRequestModel.setQuantity(56351);
        purchaseRequestModel.setProductId(12523L);
        String json = mapper.writeValueAsString(purchaseRequestModel);

        //When
        String response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/purchases/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        //Then
        assertTrue(response.contains("Could not find entity with id:"));
    }
}