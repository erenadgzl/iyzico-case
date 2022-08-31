package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.entity.Status;
import com.iyzico.challenge.exception.ProductAlreadyExistException;
import com.iyzico.challenge.exception.ProductNotFoundException;
import com.iyzico.challenge.model.ProductRequestModel;
import com.iyzico.challenge.model.ProductResponseModel;
import com.iyzico.challenge.model.SimplePage;
import com.iyzico.challenge.repository.ProductRepository;
import com.iyzico.challenge.service.implementation.DefaultProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author erenadiguzel
 */

@ExtendWith(SpringExtension.class)
class ProductServiceTest {

    @InjectMocks
    private DefaultProductService productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ModelMapper modelMapper;

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

        product = new Product();
        product.setId(1L);
        product.setName("test-product");
        product.setDescription("Test product Description");
        product.setQuantity(10);
        product.setPrice(BigDecimal.TEN);

        productResponseModel = new ProductResponseModel();
        productResponseModel.setId(1L);
        productResponseModel.setName("test-product");
        productResponseModel.setDescription("Test product Description");
        productResponseModel.setQuantity(10);
        productResponseModel.setPrice(BigDecimal.TEN);
    }

    @Test
    void should_create() throws ProductAlreadyExistException {
        //Given
        when(productRepository.findByName(any())).thenReturn(Optional.empty());
        when(modelMapper.map(any(), any())).thenReturn(product);

        //When
        Long id = productService.create(productRequestModel);

        //Then
        assertAll(
                () -> assertNotNull(id),
                () -> assertEquals(Long.valueOf(1), id)
        );
    }

    @Test
    void should_throw_product_already_exist_exception_when_create_product() {
        //Given
        when(productRepository.findByName(any())).thenReturn(Optional.of(product));

        //When
        ProductAlreadyExistException thrown = assertThrows(
                ProductAlreadyExistException.class,
                () -> productService.create(productRequestModel),
                "Expected ProductAlreadyExistException to throw, but it didn't"
        );

        //Then
        assertTrue(thrown.getMessage().contains("The product already exists!"));
    }

    @Test
    void should_get_product_model_by_id() throws ProductNotFoundException {
        //Given
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(modelMapper.map(any(), any())).thenReturn(productResponseModel);

        //When
        ProductResponseModel productResponseModel = productService.getProductModelById(1L);

        //Then
        assertAll(
                () -> assertNotNull(productResponseModel),
                () -> assertEquals(product.getId(), productResponseModel.getId()),
                () -> assertEquals(product.getName(), productResponseModel.getName()),
                () -> assertEquals(product.getDescription(), productResponseModel.getDescription()),
                () -> assertEquals(0, product.getPrice().compareTo(productResponseModel.getPrice())),
                () -> assertEquals(product.getQuantity(), productResponseModel.getQuantity())
        );
    }

    @Test
    void should_find_by_id() throws ProductNotFoundException {
        //Given
        when(productRepository.findById(any())).thenReturn(Optional.of(product));

        //When
        Product product = productService.findById(1L);

        //Then
        assertAll(
                () -> assertNotNull(product),
                () -> assertEquals(Long.valueOf(1), product.getId())
        );
    }

    @Test
    void should_throw_product_not_found_exception_when_find_by_id() {
        //Given
        when(productRepository.findById(any())).thenReturn(Optional.empty());

        //When
        ProductNotFoundException thrown = assertThrows(
                ProductNotFoundException.class,
                () -> productService.findById(123456L),
                "Expected ProductNotFoundException to throw, but it didn't"
        );

        //Then
        assertTrue(thrown.getMessage().contains("Could not find entity with id:"));
    }

    @Test
    void should_update() throws ProductNotFoundException, ProductAlreadyExistException {
        //Given
        when(productRepository.findById(any())).thenReturn(Optional.ofNullable(product));
        when(productRepository.save(any())).thenReturn(product);

        //When
        Long productId = productService.update(productRequestModel, 1L);

        //Then
        assertAll(
                () -> assertNotNull(productId),
                () -> assertEquals(Long.valueOf(1), productId)
        );
    }

    @Test
    void should_delete() throws ProductNotFoundException {
        //Given
        when(productRepository.findById(any())).thenReturn(Optional.ofNullable(product));
        when(productRepository.save(any())).thenReturn(product);

        //When
        Long productId = productService.delete(1L);

        //Then
        assertAll(
                () -> assertNotNull(productId),
                () -> assertEquals(Long.valueOf(1), productId),
                () -> assertEquals(Status.DELETED, product.getStatus())
        );
    }

    @Test
    void should_find_all() {
        //Given
        List<Product> products = new ArrayList<>();
        products.add(product);
        Page<Product> pagedResponse = new PageImpl<>(products);
        when(productRepository.findAll(PageRequest.of(0, 20))).thenReturn(pagedResponse);

        when(modelMapper.map(any(), any())).thenReturn(productResponseModel);

        //When
        SimplePage<ProductResponseModel> productResponseModels = productService.findAll(PageRequest.of(0, 20));

        //Then
        assertAll(
                () -> assertNotNull(productResponseModels),
                () -> assertEquals(1, productResponseModels.getContent().size()),
                () -> assertEquals(product.getName(), productResponseModels.getContent().get(0).getName())
        );
    }
}