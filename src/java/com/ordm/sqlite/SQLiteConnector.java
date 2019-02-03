/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ordm.sqlite;

import com.ordm.dao.impl.ConnectionManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;
import org.sqlite.JDBC;

/**
 *
 * @author Jul
 */
public class SQLiteConnector {
    static final Logger LOGGER = Logger.getLogger(SQLiteConnector.class.getName());
    
    public static Connection getConnection() { 
        Connection conn = null;
        try { 
            String url = "jdbc:sqlite:D:\\Work\\Learning\\SQLite\\sales.sqlite";
            DriverManager.registerDriver(new JDBC());
            conn = DriverManager.getConnection(url);  
 
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }
        
        return conn;
    }
    
    public static Connection getTestConnection() { 
        Connection conn = null;
        try { 
            String url = "jdbc:sqlite:D:\\Work\\Learning\\SQLite\\sales_test.sqlite";
            DriverManager.registerDriver(new JDBC());
            conn = DriverManager.getConnection(url);  
 
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }
        
        return conn;
    }
    
    private static void createOrderTable(ConnectionManager manager) throws SQLException {  
        manager.executeStatement(STATEMENT_CREATE_TABLE_ORDER, null, false);
    }
    
    public static void main(String[] args) throws SQLException {
        ConnectionManager manager = new ConnectionManager(getConnection(), true); 
  
        createOrderTable(manager);
    }
    
    
    private static final String STATEMENT_CREATE_TABLE_ORDER = new StringBuffer()
        .append("\n create table ORDM_ORDER ")
        .append("\n  (ORDER_ID INTEGER PRIMARY KEY AUTOINCREMENT ")
        .append("\n  ,DESCRIPTION VARCHAR2(4000) ")
        .append("\n  ,STATUS VARCHAR2(100)) ")
        .toString();
}
