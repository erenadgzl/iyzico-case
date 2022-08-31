package com.iyzico.challenge.entity;

import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "payment")
@Where(clause = "status <> 'DELETED'")
public class Payment  extends BaseEntity {

    private BigDecimal price;
    private String bankResponse;

    public Payment(BigDecimal price, String bankResponse) {
        this.price = price;
        this.bankResponse = bankResponse;
    }

    public Payment() {

    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getBankResponse() {
        return bankResponse;
    }

    public void setBankResponse(String bankResponse) {
        this.bankResponse = bankResponse;
    }
}
