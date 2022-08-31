package com.iyzico.challenge.controller;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.exception.ProductAlreadyExistException;
import com.iyzico.challenge.exception.ProductNotFoundException;
import com.iyzico.challenge.model.ProductRequestModel;
import com.iyzico.challenge.model.ProductResponseModel;
import com.iyzico.challenge.model.SimplePage;
import com.iyzico.challenge.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author erenadiguzel
 */


@SpringBootTest
@ExtendWith(SpringExtension.class)
class ProductControllerTest {

    @InjectMocks
    private ProductController productController;
    @Mock
    private ProductService productService;

    private Product product;
    private ProductRequestModel productRequestModel;
    private ProductResponseModel productResponseModel;

    @BeforeEach
    void init() {
        productRequestModel = new ProductRequestModel();
        productRequestModel.setName("test-product");
        productRequestModel.setDescription("Test product Description");
        productRequestModel.setQuantity(10);
        productRequestModel.setPrice(BigDecimal.TEN);

        productResponseModel = new ProductResponseModel();
        productResponseModel.setId(1L);
        productResponseModel.setName("test-product");
        productResponseModel.setDescription("Test product Description");
        productResponseModel.setQuantity(10);
        productResponseModel.setPrice(BigDecimal.TEN);

        product = new Product();
        product.setId(1L);
        product.setName("test-product");
        product.setDescription("Test product Description");
        product.setQuantity(10);
        product.setPrice(BigDecimal.TEN);
    }

    @Test
    void should_create_product() throws ProductAlreadyExistException {
        //Given
        when(productService.create(productRequestModel)).thenReturn(1L);

        //When
        ResponseEntity<Long> responseEntity = productController.createProduct(productRequestModel);

        //Then
        assertAll(
                () -> assertEquals(201, responseEntity.getStatusCodeValue()),
                () -> assertNotNull(responseEntity.getBody()),
                () -> assertEquals(Long.valueOf(1), responseEntity.getBody())
        );
    }

    @Test
    void should_get_product() throws ProductNotFoundException {
        //Given
        when(productService.getProductModelById(1L)).thenReturn(productResponseModel);

        //When
        ResponseEntity<ProductResponseModel> responseEntity = productController.getProduct(1L);

        //Then
        assertAll(
                () -> assertEquals(200, responseEntity.getStatusCodeValue()),
                () -> assertNotNull(responseEntity.getBody()),
                () -> assertEquals(productResponseModel.getId(), responseEntity.getBody().getId()),
                () -> assertEquals(productResponseModel.getName(), responseEntity.getBody().getName()),
                () -> assertEquals(productResponseModel.getDescription(), responseEntity.getBody().getDescription()),
                () -> assertEquals(0, productResponseModel.getPrice().compareTo(responseEntity.getBody().getPrice())),
                () -> assertEquals(productResponseModel.getQuantity(), responseEntity.getBody().getQuantity())

        );
    }

    @Test
    void should_update_product() throws ProductNotFoundException, ProductAlreadyExistException {
        //Given
        when(productService.update(productRequestModel, 1L)).thenReturn(1L);

        //When
        ResponseEntity<Long> responseEntity = productController.updateProduct(1L, productRequestModel);

        //Then
        assertAll(
                () -> assertEquals(200, responseEntity.getStatusCodeValue()),
                () -> assertNotNull(responseEntity.getBody()),
                () -> assertEquals(Long.valueOf(1), responseEntity.getBody())
        );
    }

    @Test
    void should_delete_product() throws ProductNotFoundException {
        //Given
        when(productService.delete(1L)).thenReturn(1L);

        //When
        ResponseEntity<Long> responseEntity = productController.deleteProduct(1L);

        //Then
        assertAll(
                () -> assertEquals(200, responseEntity.getStatusCodeValue()),
                () -> assertNotNull(responseEntity.getBody()),
                () -> assertEquals(Long.valueOf(1), responseEntity.getBody())
        );
    }

    @Test
    void should_get_all_products() {
        //Given
        Pageable pageable = PageRequest.of(0, 20);
        SimplePage<ProductResponseModel> pagedResponse = new SimplePage<>(Collections.singletonList(productResponseModel),
                1, pageable);
        when(productService.findAll(pageable)).thenReturn(pagedResponse);

        //When
        ResponseEntity<SimplePage<ProductResponseModel>> responseEntity = productController.getAllProducts(pageable);

        //Then
        assertAll(
                () -> assertEquals(200, responseEntity.getStatusCodeValue()),
                () -> assertNotNull(responseEntity.getBody()),
                () -> assertNotNull(responseEntity.getBody().getContent()),
                () -> assertEquals(1, responseEntity.getBody().getContent().size()),
                () -> assertEquals(productResponseModel.getName(), responseEntity.getBody().getContent().get(0).getName()),
                () -> assertEquals(productResponseModel.getDescription(), responseEntity.getBody().getContent().get(0).getDescription()),
                () -> assertEquals(0, productResponseModel.getPrice().compareTo(responseEntity.getBody().getContent().get(0).getPrice())),
                () -> assertEquals(productResponseModel.getQuantity(), responseEntity.getBody().getContent().get(0).getQuantity())
        );
    }
}