package com.james.SSF.Assessment2.Models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class Order {
    
    @NotEmpty(message="Required!")
    @Size(min=2, message="At least 2 char!")
    private String name;
    
    @NotEmpty(message="Required!")
    private String address;

    private Cart cart;

    private Float total;

    private String invoiceId;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public Cart getCart() {
        return cart;
    }
    public void setCart(Cart cart) {
        this.cart = cart;
    }
    public Float getTotal() {
        return total;
    }
    public void setTotal(Float total) {
        this.total = total;
    }
    public String getInvoiceId() {
        return invoiceId;
    }
    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    
    
}
