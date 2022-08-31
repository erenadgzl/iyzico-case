package com.iyzico.challenge.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author erenadiguzel
 */

@SpringBootTest
class SimplePageTest {

    private ProductResponseModel productResponseModel;

    @BeforeEach
    void init() {
        productResponseModel = new ProductResponseModel();
        productResponseModel.setId(1L);
        productResponseModel.setName("test-product");
        productResponseModel.setDescription("Test product Description");
        productResponseModel.setQuantity(10);
        productResponseModel.setPrice(BigDecimal.TEN);
    }

    @Test
    void getPage() {
        //Given
        SimplePage<ProductResponseModel> simplePage = new SimplePage<>(Collections.singletonList(productResponseModel),
                1, 1, 0, 20, Collections.singletonList("name,asc"));

        //When
        int pageNumber = simplePage.getPage();

        //Then
        assertEquals(0, pageNumber);
    }

    @Test
    void getSortList() {
        //Given
        SimplePage<ProductResponseModel> simplePage = new SimplePage<>(Collections.singletonList(productResponseModel),
                1, 1, 0, 20, Collections.singletonList("name,asc"));

        //When
        List<String> sortList = simplePage.getSortList();

        //Then
        assertNotNull(sortList);
        assertEquals(1, sortList.size());
        assertEquals("name,ASC", sortList.get(0));
    }
}