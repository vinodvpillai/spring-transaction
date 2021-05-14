package com.vinod.spring.transaction.exception;

/**
 * Customer not found exception class.
 */
public class CustomerNotFoundException extends Exception {

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
