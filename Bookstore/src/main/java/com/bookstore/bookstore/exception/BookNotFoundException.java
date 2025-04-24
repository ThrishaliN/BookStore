/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.bookstore.exception;

/**
 *
 * @author ASUS
 */
public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException() {
        super("Book not found.");
    }

    public BookNotFoundException(String message) {
        super(message);
    }
}

