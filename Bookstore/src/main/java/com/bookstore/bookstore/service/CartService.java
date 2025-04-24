/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.bookstore.service;

/**
 *
 * @author ASUS
 */
import com.bookstore.bookstore.model.Cart;
import com.bookstore.bookstore.model.CartItem;
import com.bookstore.bookstore.model.Book;
import com.bookstore.bookstore.exception.CartNotFoundException;
import com.bookstore.bookstore.exception.OutOfStockException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class CartService {

    private final Map<Integer, Cart> cartDatabase = new HashMap<>();

    @Inject
    BookService bookService;

    // Add item to a customer's cart
    public Cart addItem(Integer customerId, CartItem item) {
    Cart cart = cartDatabase.computeIfAbsent(customerId, k -> new Cart(customerId));

    Book book = bookService.getBookById(item.getBookId());
    if (book == null) {
        throw new RuntimeException("Book not found");
    }
        // Check stock availability
        if (book.getStock() < item.getQuantity()) {
            throw new OutOfStockException(book.getId());
        }

        // Add item to cart
        cart.addItem(item);
        return cart;
    }

    // Get the cart for a specific customer
    public Cart getCart(Integer customerId) {
        Cart cart = cartDatabase.get(customerId);
        if (cart == null) {
            throw new CartNotFoundException(customerId);
        }
        return cart;
    }

    // Update item quantity in the cart
    public Cart updateItemQuantity(Integer customerId, Integer bookId, int quantity) {
        Cart cart = cartDatabase.get(customerId);
        if (cart == null) {
            throw new CartNotFoundException(customerId);
        }

       // Check if the cart contains the item
        CartItem item = cart.getItem(bookId);
        if (item == null) {
            throw new RuntimeException("Item not found in the cart");
        }

        // Check stock availability
        Book book = bookService.getBookById(bookId);
        if (book == null) {
            throw new RuntimeException("Book not found");
        }
        if (book.getStock() < quantity) {
            throw new OutOfStockException(book.getId());
        }


        // Update the item quantity
        item.setQuantity(quantity);
        return cart;
    }

    // Remove item from the cart
    public void removeItem(Integer customerId, Integer bookId) {
        Cart cart = cartDatabase.get(customerId);
        if (cart == null) {
            throw new CartNotFoundException(customerId);
        }

        // Remove the item from the cart
        cart.removeItem(bookId);
    }
}


