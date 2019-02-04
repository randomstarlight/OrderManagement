package com.ordm.dao;

import com.ordm.dao.impl.OrderDAOImpl;

/**
 * Factory class that returns DAO objects for use in services
 *
 * @author Jul
 */
public class DAOFactory {
    public static OrderDAO getOrderDAO() {
        return new OrderDAOImpl();
    }
}
