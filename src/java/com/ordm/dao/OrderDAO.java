/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ordm.dao;

import com.ordm.dao.beans.Order;
import com.ordm.exception.ORDMStorageException;

/**
 *
 * @author Jul
 */
public interface OrderDAO extends DAO<Order> {
    public void forwardStatus(Object id) throws ORDMStorageException;
}
