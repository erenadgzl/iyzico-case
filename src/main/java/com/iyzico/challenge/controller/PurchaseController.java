package com.iyzico.challenge.controller;

import com.iyzico.challenge.exception.OutOfStockException;
import com.iyzico.challenge.exception.ProductNotFoundException;
import com.iyzico.challenge.model.PurchaseRequestModel;
import com.iyzico.challenge.service.PurchaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author erenadiguzel
 */

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping
    public ResponseEntity<Long> createPurchase(@RequestBody @Valid PurchaseRequestModel purchaseRequestModel) throws ProductNotFoundException, OutOfStockException {
        return new ResponseEntity<>(purchaseService.createPurchase(purchaseRequestModel), HttpStatus.CREATED);
    }
}
