package com.iyzico.challenge.entity;

import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author erenadiguzel
 */

@Entity
@Table(name = "product")
@Where(clause = "status <> 'DELETED'")
public class Product extends BaseEntity {

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String description;

    @Column(nullable=false)
    private BigDecimal price;

    @Column(nullable=false)
    private Integer quantity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Transient
    public boolean isStockAvailable(Integer purchaseQuantity){
        return quantity > 0 && quantity >= purchaseQuantity;
    }

    @Transient
    public synchronized void reduceStock(Integer purchaseQuantity){
        quantity -= purchaseQuantity;
    }
}
