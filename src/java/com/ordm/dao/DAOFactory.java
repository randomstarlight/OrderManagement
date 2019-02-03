/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ordm.dao;

import com.ordm.dao.impl.OrderDAOImpl;

/**
 *
 * @author Jul
 */
public class DAOFactory {
    public static OrderDAO getOrderDAO() {
        return new OrderDAOImpl();
    }
}
