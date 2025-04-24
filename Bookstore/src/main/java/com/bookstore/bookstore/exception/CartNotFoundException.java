/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.bookstore.exception;

/**
 *
 * @author ASUS
 */
public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(Integer customerId) {
        super("Cart not found for customer with ID: " + customerId);
    }
}

