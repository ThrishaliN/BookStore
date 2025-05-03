/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.bookstore.service;

import com.bookstore.bookstore.model.Book;
import com.bookstore.bookstore.model.Cart;
import com.bookstore.bookstore.model.CartItem;
import com.bookstore.bookstore.exception.CartNotFoundException;
import com.bookstore.bookstore.exception.InvalidInputException;
import com.bookstore.bookstore.exception.OutOfStockException;

import java.util.HashMap;
import java.util.Map;

public class CartService {

    private static final Map<Integer, Cart> cartMap = new HashMap<>();

    private final CustomerService customerService = new CustomerService();

    public Cart addItemToCart(int customerId, CartItem item) throws InvalidInputException {
        validateCustomer(customerId);
        validateQuantity(item.getQuantity());

        Book book = BookService.getBookById(item.getBookId());
        if (book == null) {
            throw new InvalidInputException("Book not found.");
        }
        if (item.getQuantity() > book.getStock()) {
            throw new InvalidInputException("Insufficient stock.");
        }

        Cart cart = cartMap.computeIfAbsent(customerId, Cart::new);

        CartItem existingItem = cart.getItem(item.getBookId());
        if (existingItem != null) {
            int totalQty = existingItem.getQuantity() + item.getQuantity();
            if (totalQty > book.getStock()) {
                throw new OutOfStockException("Not enough stock to update item.");
            }
            existingItem.setQuantity(totalQty);
        } else {
            item.setBook(book);
            cart.addItem(item);
        }

        return cart;
    }

    public Cart getCart(int customerId) throws InvalidInputException {
        validateCustomer(customerId);

        // Always return a cart (even if empty) instead of throwing
        return cartMap.computeIfAbsent(customerId, Cart::new);
    }
    

    public Cart updateItemQuantity(int customerId, int bookId, int newQuantity) throws InvalidInputException, CartNotFoundException {
        validateCustomer(customerId);
        validateQuantity(newQuantity);

        Book book = BookService.getBookById(bookId);
        if (book == null) {
            throw new InvalidInputException("Book not found.");
        }
        if (newQuantity > book.getStock()) {
            throw new InvalidInputException("Insufficient stock.");
        }

        Cart cart = cartMap.get(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart not found for customer ID: " + customerId);
        }

        CartItem item = cart.getItem(bookId);
        if (item == null) {
            throw new InvalidInputException("Item not found in cart.");
        }

        item.setQuantity(newQuantity);
        return cart;
    }

    public void removeItem(int customerId, int bookId) throws InvalidInputException, CartNotFoundException {
        validateCustomer(customerId);

        Cart cart = cartMap.get(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart not found for customer ID: " + customerId);
        }

        CartItem item = cart.getItem(bookId);
        if (item == null) {
            throw new InvalidInputException("Item not found in cart.");
        }

        cart.removeItem(bookId);
    }

    private void validateCustomer(int customerId) throws InvalidInputException {
        customerService.getCustomerById(customerId)
                .orElseThrow(() -> new InvalidInputException("Customer not found with ID: " + customerId));
    }

    private void validateQuantity(int quantity) throws InvalidInputException {
        if (quantity <= 0) {
            throw new InvalidInputException("Quantity must be greater than zero.");
        }
    }
}





