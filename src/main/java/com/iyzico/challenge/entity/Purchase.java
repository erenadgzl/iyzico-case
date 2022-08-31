package com.iyzico.challenge.entity;

import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author erenadiguzel
 */

@Entity
@Table(name = "purchase")
@Where(clause = "status <> 'DELETED'")
public class Purchase extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @Column
    private String customerId;

    public Purchase() {
    }

    public Purchase(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
        this.customerId = UUID.randomUUID().toString();
        this.totalPrice = calculateTotalPrice();
    }

    public Product getProduct() {
        return product;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    @Transient
    public BigDecimal calculateTotalPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
