package com.iyzico.challenge.controller;

import com.iyzico.challenge.exception.ProductAlreadyExistException;
import com.iyzico.challenge.exception.ProductNotFoundException;
import com.iyzico.challenge.model.ProductRequestModel;
import com.iyzico.challenge.model.ProductResponseModel;
import com.iyzico.challenge.model.SimplePage;
import com.iyzico.challenge.service.ProductService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author erenadiguzel
 */

@RestController
@RequestMapping("products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Long> createProduct(@RequestBody @Valid ProductRequestModel productRequestModel) throws ProductAlreadyExistException {
        return new ResponseEntity<>(productService.create(productRequestModel), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseModel> getProduct(@PathVariable Long id) throws ProductNotFoundException {
        return ResponseEntity.ok(productService.getProductModelById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductRequestModel productRequestModel) throws ProductNotFoundException, ProductAlreadyExistException {
        return ResponseEntity.ok(productService.update(productRequestModel, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteProduct(@PathVariable Long id) throws ProductNotFoundException {
        return ResponseEntity.ok(productService.delete(id));
    }

    @GetMapping("/all")
    public ResponseEntity<SimplePage<ProductResponseModel>> getAllProducts(
            @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok(productService.findAll(pageable));
    }
}
