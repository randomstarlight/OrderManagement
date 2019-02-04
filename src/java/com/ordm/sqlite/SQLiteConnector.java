/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ordm.sqlite;

import com.ordm.dao.impl.ConnectionManager;
import java.io.File;
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
    
    /**
     * Static method that returns a SQLite Connection object using a relative path which will create the connection in Catalina home
     * The method will check if the SQLite DB exists, if not it will create the file and go through the initial setup (in this case the creation of the order table)
     *
     * @return
     */
    public static Connection getConnection() { 
        Connection conn = null;
        
        File file = new File("sales.sqlite"); 
        boolean newDb = !file.exists();
        try { 
            String url = "jdbc:sqlite:sales.sqlite";
            DriverManager.registerDriver(new JDBC());
            conn = DriverManager.getConnection(url);  
            if (newDb) {
                initSetup(conn);
            }
 
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }
        
        return conn;
    }
    
    public static Connection getTestConnection() { 
        Connection conn = null;
        try { 
            String url = "sales_test.sqlite";
            DriverManager.registerDriver(new JDBC());
            conn = DriverManager.getConnection(url);  
 
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }
        
        return conn;
    }
    
    private static void initSetup(Connection conn) throws SQLException {
        createOrderTable(conn);
    }
    
    private static void createOrderTable(Connection conn) throws SQLException {  
        ConnectionManager manager = new ConnectionManager(conn, true);
        
        try {
            manager.getResultSet(CHECK_FOR_ORDER_TAB, null);
        }
        catch(org.sqlite.SQLiteException e) {
            manager.executeStatement(STATEMENT_CREATE_TABLE_ORDER, null, false);               
        }
    }    
    
    private static final String STATEMENT_CREATE_TABLE_ORDER = new StringBuffer()
        .append("\n create table ORDM_ORDER ")
        .append("\n  (ORDER_ID INTEGER PRIMARY KEY AUTOINCREMENT ")
        .append("\n  ,DESCRIPTION VARCHAR2(4000) ")
        .append("\n  ,STATUS VARCHAR2(100)) ")
        .toString();
    
    private static final String CHECK_FOR_ORDER_TAB = new StringBuffer()
        .append("\n select 1 from ORDM_ORDER ")
        .toString();
}
