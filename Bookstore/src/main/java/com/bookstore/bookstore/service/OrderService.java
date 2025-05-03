/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.bookstore.service;

/**
 *
 * @author ASUS
 */

import com.bookstore.bookstore.exception.CartNotFoundException;
import com.bookstore.bookstore.exception.CustomerNotFoundException;
import com.bookstore.bookstore.exception.InvalidInputException;
import com.bookstore.bookstore.model.Cart;
import com.bookstore.bookstore.model.CartItem;
import com.bookstore.bookstore.model.Customer;
import com.bookstore.bookstore.model.Order;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderService {

    private static final List<Order> orders = new ArrayList<>();
    private static final AtomicInteger orderIdCounter = new AtomicInteger(1);

    private final CartService cartService = new CartService();
    private final CustomerService customerService = new CustomerService();

    // Place a new order
    public Order placeOrder(int customerId) {
        if (customerId <= 0) {
            throw new InvalidInputException("Customer ID must be a positive number.");
        }

        Customer customer = customerService.getCustomerById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + customerId));

        Cart cart = cartService.getCart(customerId);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new CartNotFoundException("Cart is empty or not found.");
        }

        double totalAmount = 0.0;

        // Check stock and prepare order items
        for (CartItem cartItem : cart.getItems()) {
            if (cartItem.getQuantity() > cartItem.getBook().getStock()) {
                throw new InvalidInputException("Insufficient stock for book ID: " + cartItem.getBook().getId());
            }

            // Deduct stock
            cartItem.getBook().setStock(cartItem.getBook().getStock() - cartItem.getQuantity());
            totalAmount += cartItem.getQuantity() * cartItem.getBook().getPrice();
        }

        // Create new order
        Order newOrder = new Order();
        newOrder.setId(orderIdCounter.getAndIncrement());
        newOrder.setCustomer(customer);
        newOrder.setTotalAmount(totalAmount);
        newOrder.setOrderDate(LocalDateTime.now());

        // Set status 
        newOrder.setStatus("Pending");
        
        // Create a deep copy of the cart items
        List<CartItem> copiedItems = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            CartItem copy = new CartItem();
            copy.setBook(item.getBook()); // If needed, you can copy more safely
            copy.setBookId(item.getBookId());
            copy.setQuantity(item.getQuantity());
            copiedItems.add(copy);
        }

        newOrder.setItems(copiedItems); // Now setting a copy, not the original cart list

        
        orders.add(newOrder);

        // Clear the cart after placing the order
        cart.getItems().clear();

        return newOrder;
    }

    // Get all orders
    public List<Order> getAllOrders() {
        return orders;
    }

    // Get order by ID
    public Optional<Order> getOrderById(int orderId) {
        return orders.stream()
                .filter(order -> order.getId() == orderId)
                .findFirst();
    }

    // Check if a customer exists
    public boolean doesCustomerExist(int customerId) {
        return customerService.getCustomerById(customerId).isPresent();
    }
}
