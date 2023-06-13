package com.jcanseco.inventoryapi.exceptions;
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
