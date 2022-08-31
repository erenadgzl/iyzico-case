package com.iyzico.challenge.model;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * @author erenadiguzel
 */

public class ProductRequestModel {

    @NotNull(message = "Name can not be null!")
    @Size(
            min = 2,
            max = 30,
            message = "Name '${validatedValue}' must be between {min} and {max} characters long"
    )
    private String name;

    @NotNull(message = "Description can not be null!")
    @Size(
            min = 2,
            max = 200,
            message = "Description '${validatedValue}' must be between {min} and {max} characters long"
    )
    private String description;

    @NotNull(message = "Price can not be null!")
    @DecimalMin(
            value = "0",
            message = "Price must be higher than 0"
    )
    private BigDecimal price;

    @NotNull(message = "Quantity can not be null!")
    @Min(
            value = 0,
            message = "Quantity must be higher than 0"
    )
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
}
