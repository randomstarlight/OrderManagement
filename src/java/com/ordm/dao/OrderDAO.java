package com.ordm.dao;

import com.ordm.dao.beans.Order;
import com.ordm.exception.ORDMStorageException;

/**
 *
 * Extension of the generic DAO interface that is specific to an Order object
 * The Impl class must implement all generic DAO methods as well as status forwarding
 * 
 * @author Jul
 */
public interface OrderDAO extends DAO<Order> {
    public Order forwardStatus(Object id) throws ORDMStorageException;
}
