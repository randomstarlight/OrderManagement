/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ordm.exception;

/**
 *
 * @author Jul
 */
public class ORDMStorageException extends Exception {
    Exception e;

    public ORDMStorageException(Exception e) {
        this.e = e;
    }   
}
