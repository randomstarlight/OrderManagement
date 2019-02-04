package com.ordm.dao;

import com.ordm.exception.ORDMStorageException;
import java.util.List;

/**
 * Generic representation of a data access object. Can we adapted to work with any type of storage
 *
 * @author Jul
 * @param <T> The bean representation of the object that will be manipulated
 */
public interface DAO<T> {    
    
    T get(Object id) throws ORDMStorageException;
     
    List<T> list() throws ORDMStorageException;
     
    T create(T t) throws ORDMStorageException;
     
    void update(Object id, T t) throws ORDMStorageException;
     
    void remove(Object id) throws ORDMStorageException;
}
