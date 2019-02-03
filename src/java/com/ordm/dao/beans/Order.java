/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ordm.dao.beans;

import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author Jul
 */
public class Order {
    BigDecimal id;
    String description;
    int status;

    public Order() {
    }

    public Order(BigDecimal id, String description, int status) {
        this.id = id;
        this.description = description;
        this.status = status;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Order other = (Order) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    } 
    
    public static final int STATUS_NEW = 1;
    public static final int STATUS_PROCESSING = 2;
    public static final int STATUS_READY = 3;
    public static final int STATUS_DISPATCHED = 4;
    public static final int STATUS_DELIVERED = 5;
    
}
