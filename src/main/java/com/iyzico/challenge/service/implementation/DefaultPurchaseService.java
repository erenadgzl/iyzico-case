package com.iyzico.challenge.service.implementation;

import com.iyzico.challenge.entity.Purchase;
import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.exception.OutOfStockException;
import com.iyzico.challenge.exception.ProductNotFoundException;
import com.iyzico.challenge.model.PurchaseRequestModel;
import com.iyzico.challenge.repository.PurchaseRepository;
import com.iyzico.challenge.service.IyzicoPaymentService;
import com.iyzico.challenge.service.PurchaseService;
import com.iyzico.challenge.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author erenadiguzel
 */

@Service
public class DefaultPurchaseService implements PurchaseService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultPurchaseService.class);

    private final PurchaseRepository purchaseRepository;
    private final ProductService productService;
    private final IyzicoPaymentService iyzicoPaymentService;

    public DefaultPurchaseService(PurchaseRepository purchaseRepository, ProductService productService, IyzicoPaymentService iyzicoPaymentService) {
        this.purchaseRepository = purchaseRepository;
        this.productService = productService;
        this.iyzicoPaymentService = iyzicoPaymentService;
    }

    /**
     * Add purchase
     *
     * @param purchaseRequestModel Contains purchasing information
     * @return
     * @throws ProductNotFoundException Thrown when product not found given id
     * @throws OutOfStockException Thrown when the quantity to be purchased is not in stock
     */
    @Override
    @Transactional
    public Long createPurchase(PurchaseRequestModel purchaseRequestModel) throws ProductNotFoundException, OutOfStockException {
        Product product = productService.findById(purchaseRequestModel.getProductId());
        if (!product.isStockAvailable(purchaseRequestModel.getQuantity())) {
            logger.warn("This product is out of stock!");
            throw new OutOfStockException("This product is out of stock!");
        }
        product.reduceStock(purchaseRequestModel.getQuantity());

        Purchase purchase = new Purchase(product, purchaseRequestModel.getQuantity());
        purchase = purchaseRepository.save(purchase);
        logger.info("Purchase saved successfully!");

        iyzicoPaymentService.pay(purchase.getTotalPrice());
        return purchase.getId();
    }
}
