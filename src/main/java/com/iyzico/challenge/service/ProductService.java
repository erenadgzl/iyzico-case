package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.exception.ProductAlreadyExistException;
import com.iyzico.challenge.exception.ProductNotFoundException;
import com.iyzico.challenge.model.ProductRequestModel;
import com.iyzico.challenge.model.ProductResponseModel;
import com.iyzico.challenge.model.SimplePage;
import org.springframework.data.domain.Pageable;

/**
 * @author erenadiguzel
 */

public interface ProductService {
    Long create(ProductRequestModel productDTO) throws ProductAlreadyExistException;

    ProductResponseModel getProductModelById(Long id) throws ProductNotFoundException;

    Product findById(Long id) throws ProductNotFoundException;

    Long update(ProductRequestModel productDTO, Long id) throws ProductNotFoundException, ProductAlreadyExistException;

    Long delete(Long id) throws ProductNotFoundException;

    SimplePage<ProductResponseModel> findAll(Pageable pageable);
}
