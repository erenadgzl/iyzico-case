package com.iyzico.challenge.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author erenadiguzel
 */

public class PurchaseRequestModel {

    @NotNull(message = "Product Id can not be null!")
    private Long productId;

    @NotNull(message = "Quantity can not be null!")
    @Min(
            value = 0,
            message = "Quantity must be higher than 0"
    )
    private int quantity;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
