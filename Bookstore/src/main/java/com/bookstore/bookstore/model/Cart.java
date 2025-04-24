/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.bookstore.model;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ASUS
 */
public class Cart {
    
    private int customerId;
    private List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public Cart(int customerId) {
        this.customerId = customerId;
        this.items = new ArrayList<>();
    }

    public int getCustomerId() {
        return customerId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    // Add item to cart
    public void addItem(CartItem item) {
        // Check if book already exists in cart, update quantity instead
        for (CartItem existingItem : items) {
            if (existingItem.getBookId() == item.getBookId()) {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                return;
            }
        }
        this.items.add(item);
    }

    // Remove item by bookId
    public void removeItem(int bookId) {
        items.removeIf(item -> item.getBookId() == bookId);
    }

    // Update quantity for a specific bookId
    public void updateItemQuantity(int bookId, int newQuantity) {
        for (CartItem item : items) {
            if (item.getBookId() == bookId) {
                item.setQuantity(newQuantity);
                return;
            }
        }
    }
    public CartItem getItem(int bookId) {
        for (CartItem item : items) {
            if (item.getBookId() == bookId) {
                return item;
            }
        }
        return null;
    }

    // Clear the cart
    public void clear() {
        items.clear();
    }

}


