/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ordm.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jul
 */
public class ConnectionManager {
    static final Logger LOGGER = Logger.getLogger(ConnectionManager.class.getName());
    final Connection conn;
    PreparedStatement ps;
    ResultSet rs;
    int fetchSize;
    boolean isAutoCommit;
    
    public ConnectionManager(Connection conn, boolean isAutoCommit) {
        this.conn = conn;
        this.isAutoCommit = isAutoCommit;
    }

    public ConnectionManager(Connection conn, int fetchSize) {
        this.conn = conn;
        this.fetchSize = fetchSize;
    }

    public void bindValues(Map<Integer, Object> values)
            throws SQLException {
        if (values != null && !values.isEmpty()) {
            Iterator iterator = values.entrySet().iterator();
            for (Iterator itr = iterator; itr.hasNext();) {
                Map.Entry entry = (Map.Entry) iterator.next();
                int key = (Integer) entry.getKey();
                Object value = entry.getValue();

                if (value == null) {
                    ps.setObject(key, null);
                } else if (value instanceof Integer || value instanceof Long || value instanceof BigDecimal) {
                    ps.setInt(key, Integer.valueOf(entry.getValue().toString()));
                } else if (value instanceof Timestamp) {
                    ps.setTimestamp(key, (Timestamp) value);
                } else {
                    ps.setString(key, value.toString());
                }
            }
        }
    }

    public void logStatement(String sqlStatement, Map<String, Object> values) {
        StringBuffer bindValues = new StringBuffer();
        boolean first = true;

        if (values != null && !values.isEmpty()) {
            bindValues.append(" bind : [");

            for (Map.Entry<String, Object> entry : values.entrySet()) {
                Object value = entry.getValue();

                if (first) {
                    first = false;
                } else {
                    bindValues.append(", ");
                }

                if (value == null) {
                    bindValues.append(' ');
                } else {
                    bindValues.append(value.toString());
                }
            }
            bindValues.append(']');
        }

        String msg = "Executing query: " + sqlStatement + bindValues.toString();
        LOGGER.log(Level.INFO, msg);
    }

    public ResultSet getResultSet(String sqlStatement, Map values) throws SQLException {
        logStatement(sqlStatement, values);
        long startTime = System.currentTimeMillis();

        ps = conn.prepareStatement(sqlStatement);
        if (fetchSize > 0) {
            ps.setFetchSize(fetchSize);
        }
        if (values != null) {
            bindValues(values);
        }
        rs = ps.executeQuery();
        LOGGER.log(Level.INFO, new StringBuffer().append("Total query time : ")
                .append((System.currentTimeMillis() - startTime))
                .toString());

        return rs;
    }

    private int executeUpdate(String sqlStatement, Map values, boolean returnGenKeys) throws SQLException {
        logStatement(sqlStatement, values);
        this.ps = returnGenKeys ? this.conn.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS) : this.conn.prepareStatement(sqlStatement);        
        bindValues(values);
        return this.ps.executeUpdate();
    }

    public int executeStatement(String sqlStatement, Map values, boolean returnGenKeys) throws SQLException {
        logStatement(sqlStatement, values);
        int recordsChanged = 0;
        recordsChanged = executeUpdate(sqlStatement.trim(), values, returnGenKeys);

        if (!isAutoCommit) {
            this.conn.commit();
        }
        return recordsChanged;
    }

    private void rollback() {
        try {
            // close connection
            if (this.conn != null && !this.conn.isClosed()) {
                this.conn.rollback();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Error during Rollback the current database transaction.", e);
        }
    }

    public void close() throws SQLException {
        if (ps != null) {
            ps.close();
        }
        if (rs != null) {
            rs.close();
        }
        if (conn != null && !this.conn.isClosed()) {
            conn.close();
        }
    }

    public int getFetchSize() {
        return fetchSize;
    }

    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }
}
