package com.iyzico.challenge.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.exception.ProductNotFoundException;
import com.iyzico.challenge.model.ProductRequestModel;
import com.iyzico.challenge.model.ProductResponseModel;
import com.iyzico.challenge.model.SimplePage;
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
class ProductControllerIntegrationTest {

    @Autowired
    private DefaultProductService productService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    private Long productId;
    private ProductRequestModel productRequestModel;

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

        productId = null;
        productRequestModel = null;
    }


    @Test
    void should_create_product() throws Exception {
        //Given
        String json = mapper.writeValueAsString(productRequestModel);

        //When
        String response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/products/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        this.productId = Long.valueOf(response);

        //Then
        assertAll(
                () -> assertNotNull(productId)
        );
    }

    @Test
    void should_throw_product_already_exist_exception_when_create_product() throws Exception {
        //Given
        this.productId = productService.create(productRequestModel);
        String json = mapper.writeValueAsString(productRequestModel);

        //When
        String response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/products/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andReturn().getResponse().getContentAsString();

        //Then
        assertTrue(response.contains("The product already exists!"));
    }

    @Test
    void should_get_product() throws Exception {
        //Given
        this.productId = productService.create(productRequestModel);

        //When
        String response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/products/" + productId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ProductResponseModel productResponseModel = mapper.readValue(response, ProductResponseModel.class);

        //Then
        assertAll(
                () -> assertNotNull(productResponseModel),
                () -> assertEquals(productId, productResponseModel.getId()),
                () -> assertEquals(productRequestModel.getName(), productResponseModel.getName()),
                () -> assertEquals(productRequestModel.getDescription(), productResponseModel.getDescription()),
                () -> assertEquals(productRequestModel.getQuantity(), productResponseModel.getQuantity()),
                () -> assertEquals(0, productRequestModel.getPrice().compareTo(productResponseModel.getPrice()))
        );
    }

    @Test
    void should_throw_product_not_found_exception_when_get_product() throws Exception {
        //Given
        long productId = 1424245242L;

        //When
        String response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/products/" + productId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        //Then
        assertTrue(response.contains("Could not find entity with id:"));
    }

    @Test
    void should_update_product() throws Exception {
        //Given
        Long productId = productService.create(productRequestModel);
        productRequestModel.setName("test update");
        productRequestModel.setDescription("test update detail");
        productRequestModel.setQuantity(123);
        productRequestModel.setPrice(BigDecimal.valueOf(863));
        String json = mapper.writeValueAsString(productRequestModel);

        //When
        String response = mockMvc.perform(MockMvcRequestBuilders
                        .put("/products/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        productId = Long.valueOf(response);
        this.productId = productId;
        Product product = productService.findById(productId);

        //Then
        assertAll(
                () -> assertNotNull(product),
                () -> assertEquals(productRequestModel.getName(), product.getName()),
                () -> assertEquals(productRequestModel.getDescription(), product.getDescription()),
                () -> assertEquals(productRequestModel.getQuantity(), product.getQuantity()),
                () -> assertEquals(0, productRequestModel.getPrice().compareTo(product.getPrice()))
        );
    }

    @Test
    void should_delete_product() throws Exception {
        //Given
        Long productId = productService.create(productRequestModel);

        //When
        String response = mockMvc.perform(MockMvcRequestBuilders
                        .delete("/products/" + productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long deletedProductId = Long.valueOf(response);

        //Then
        assertAll(
                () -> assertNotNull(deletedProductId),
                () -> assertEquals(productId, deletedProductId)
        );
    }

    @Test
    void should_get_all_products() throws Exception {
        //Given
        this.productId = productService.create(productRequestModel);

        //When
        String response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/products/all"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        SimplePage<ProductResponseModel> productResponseModels = mapper.readValue(response, new TypeReference<SimplePage<ProductResponseModel>>() {
        });

        //Then
        assertAll(
                () -> assertNotNull(productResponseModels),
                () -> assertNotNull(productResponseModels.getContent()),
                () -> assertEquals(1, productResponseModels.getContent().size()),
                () -> assertEquals(productRequestModel.getName(), productResponseModels.getContent().get(0).getName()),
                () -> assertEquals(productRequestModel.getName(), productResponseModels.getContent().get(0).getName()),
                () -> assertEquals(productRequestModel.getDescription(), productResponseModels.getContent().get(0).getDescription()),
                () -> assertEquals(productRequestModel.getQuantity(), productResponseModels.getContent().get(0).getQuantity()),
                () -> assertEquals(0, productRequestModel.getPrice().compareTo(productResponseModels.getContent().get(0).getPrice()))
        );
    }
}