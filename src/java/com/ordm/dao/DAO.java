/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ordm.dao;

import com.ordm.exception.ORDMStorageException;
import java.util.List;

/**
 *
 * @author Jul
 * @param <T>
 */
public interface DAO<T> {    
    T get(Object id) throws ORDMStorageException;
     
    List<T> list() throws ORDMStorageException;
     
    T create(T t) throws ORDMStorageException;
     
    void update(Object id, T t) throws ORDMStorageException;
     
    void remove(Object id) throws ORDMStorageException;
}
