package com.iyzico.challenge.service;

import com.iyzico.challenge.exception.OutOfStockException;
import com.iyzico.challenge.exception.ProductNotFoundException;
import com.iyzico.challenge.model.PurchaseRequestModel;

/**
 * @author erenadiguzel
 */
public interface PurchaseService {
    Long createPurchase(PurchaseRequestModel purchaseRequestModel) throws ProductNotFoundException, OutOfStockException;
}
