package com.ordm.dao.impl;

import com.ordm.dao.OrderDAO;
import com.ordm.dao.beans.Order;
import com.ordm.exception.ORDMStorageException;
import com.ordm.sqlite.SQLiteConnector;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the DAO interface specific to Order objects.
 *
 * @author Jul
 */
public class OrderDAOImpl implements OrderDAO {
    final ConnectionManager manager = new ConnectionManager(SQLiteConnector.getConnection(), true);

    public OrderDAOImpl() {
    }

    /**
     * Method that returns an order based on its id
     *
     * @param id id of the Order
     * @return Order object with the provided id
     * @throws ORDMStorageException
     */
    @Override
    public Order get(Object id) throws ORDMStorageException {
        Order order = null;
        try {
            order = this.getOrderById(id);
            manager.close();
            
        } catch (SQLException ex) {
            throw new ORDMStorageException(ex);
        }
        
        return order;
    }

    /**
     * Method that lists all available orders
     *
     * @return An ArrayList with all orders in the DB
     * @throws ORDMStorageException
     */
    @Override
    public List<Order> list() throws ORDMStorageException {
        List<Order> orders = new ArrayList<>();
        try {
            
            ResultSet rs = manager.getResultSet(STATEMENT_ORDER_LIST, null);
            while(rs.next()) {
                Order order = new Order(rs.getBigDecimal("ORDER_ID"), rs.getString("DESCRIPTION"), rs.getBigDecimal("STATUS").intValue());
                
                orders.add(order);
            }
             
            manager.close();
        } catch (SQLException ex) {
            throw new ORDMStorageException(ex);
        }
        
        return orders;
    }

    /**
     * Method that creates an order in the DB based on the Order object provided
     *
     * @param order
     * @return an Order object representing the new order created together with newly generated key
     * @throws ORDMStorageException
     */
    @Override
    public Order create(Order order) throws ORDMStorageException { 
        try {
            Map<Integer, Object> values = new HashMap<>();
            order.setStatus(Order.STATUS_NEW);
            values.put(1, order.getDescription());
            values.put(2, order.getStatus());
            
            manager.executeStatement(STATEMENT_ORDER_CREATE, values, true);
            
            ResultSet rs = manager.ps.getGeneratedKeys();
            if (rs.next()) {
                order.setId(new BigDecimal(rs.getInt(1)));
            }
             
            manager.close();            
        } catch (SQLException ex) {
            throw new ORDMStorageException(ex);
        }
        
        return order;
    }

    /**
     * Method that updates order fields based on the fields available in the Order object provided
     * For demonstration purposes, the only field that can be updated is the Description field, others could be added provided the data model is more complex
     * The id and status should not be updated via this method
     *
     * @param id unique id of the order
     * @param order Order object with fields to be updated
     * @throws ORDMStorageException
     */
    @Override
    public void update(Object id, Order order) throws ORDMStorageException {        
        try {
            Map<Integer, Object> values = new HashMap<>();
            values.put(1, order.getDescription());
            values.put(2, id);
            
            manager.executeStatement(STATEMENT_ORDER_UPDATE, values, false);
             
            manager.close();
            
        } catch (SQLException ex) {
            throw new ORDMStorageException(ex);
        }   
    }
    
    /**
     * Method that increments the status of Order, allowing it to go forward to the next step
     *
     * @param id id of the Order
     * @return an Order object with the new status attached
     * @throws ORDMStorageException
     */
    @Override
    public Order forwardStatus(Object id) throws ORDMStorageException {         
        try {
            Order order = this.getOrderById(id);
            int status = order.getStatus();
            status++;
            order.setStatus(status);
            
            Map<Integer, Object> values = new HashMap<>();
            values.put(1, status);
            values.put(2, id);
            
            manager.executeStatement(STATEMENT_ORDER_UPDATE_STATUS, values, false);
             
            manager.close();
            return order;
            
        } catch (SQLException ex) {
            throw new ORDMStorageException(ex);
        }  
    }

    /**
     * Method that removes an Order from the DB
     *
     * @param id id of the Order
     * @throws ORDMStorageException
     */
    @Override
    public void remove(Object id) throws ORDMStorageException {
        try {
            Map<Integer, Object> values = new HashMap<>();
            values.put(1, id);
            
            manager.executeStatement(STATEMENT_ORDER_DELETE, values, false);             
            manager.close();
            
        } catch (SQLException ex) {
            throw new ORDMStorageException(ex);
        }
    }
    
    /**
     * Private method that returns an order based on the order id that can only be used by the other public methods
     * 
     * @param id
     * @throws SQLException
     */
    private Order getOrderById(Object id) throws SQLException {
        Order order = new Order();
        Map<Integer, Object> values = new HashMap<>();
        values.put(1, id);

        ResultSet rs = manager.getResultSet(STATEMENT_ORDER_GET, values);
        while(rs.next()) {
            order.setId(rs.getBigDecimal("ORDER_ID"));
            order.setDescription(rs.getString("DESCRIPTION"));
            order.setStatus(rs.getBigDecimal("STATUS").intValue());
        }
        
        return order;
    }
    
    private static final String STATEMENT_ORDER_DELETE = new StringBuffer()
            .append(" \n delete from ORDM_ORDER ")
            .append(" \n where ORDER_ID = ? ")
            .toString();    
    
    private static final String STATEMENT_ORDER_UPDATE_STATUS = new StringBuffer()
            .append(" \n update ORDM_ORDER ")
            .append(" \n set  STATUS  = ? ")
            .append(" \n where ORDER_ID = ? ")
            .toString();
    
    private static final String STATEMENT_ORDER_UPDATE = new StringBuffer()
            .append(" \n update ORDM_ORDER ")
            .append(" \n set  DESCRIPTION  = ? ")
            .append(" \n where ORDER_ID = ? ")
            .toString();
    
    private static final String STATEMENT_ORDER_CREATE = new StringBuffer()
            .append(" \n insert into ORDM_ORDER ")
            .append(" \n    ( DESCRIPTION ")
            .append(" \n    , STATUS ) ")
            .append(" \n values (?, ?) ")
            .toString();
    
    private static final String STATEMENT_ORDER_GET = new StringBuffer()
            .append(" \n select ORDER_ID ")
            .append(" \n      , DESCRIPTION ")
            .append(" \n      , STATUS ")
            .append(" \n from   ORDM_ORDER ")
            .append(" \n where  ORDER_ID = ? ")
            .toString();
    
    private static final String STATEMENT_ORDER_LIST = new StringBuffer()
            .append(" \n select ORDER_ID ")
            .append(" \n      , DESCRIPTION ")
            .append(" \n      , STATUS ")
            .append(" \n from   ORDM_ORDER ")
            .toString();
    
}
