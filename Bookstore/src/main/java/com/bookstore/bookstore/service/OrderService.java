/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.bookstore.service;

/**
 *
 * @author ASUS
 */
import com.bookstore.bookstore.exception.*;
import com.bookstore.bookstore.model.*;


import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class OrderService {

    private static final Logger LOGGER = Logger.getLogger(OrderService.class.getName());

    private final Map<Integer, Order> orderStore = new HashMap<>();
    private final AtomicInteger orderIdCounter = new AtomicInteger(1);

    private final CustomerService customerService = new CustomerService();
    private final CartService cartService = new CartService();
    private final BookService bookService = new BookService();

    public Order createOrder(Integer customerId) {
        Customer customer = customerService.getCustomerById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        Cart cart = cartService.getCartByCustomerId(customerId)
                .orElseThrow(() -> new CartNotFoundException(customerId));

        if (cart.getItems().isEmpty()) {
            throw new InvalidInputException("Cart is empty. Cannot place order.");
        }

        double total = 0.0;
        for (CartItem item : cart.getItems()) {
            Book book = bookService.getBookById(item.getBookId())
                    .orElseThrow(() -> new BookNotFoundException(String.valueOf(item.getBookId())));


            if (book.getStock() < item.getQuantity()) {
                throw new OutOfStockException("Book '" + book.getTitle() + "' is out of stock.");
            }

            book.setStock(book.getStock() - item.getQuantity());
            item.setBook(book); // Optional: set book info into item for order history
            total += book.getPrice() * item.getQuantity();
        }

        Order order = new Order(
                orderIdCounter.getAndIncrement(),
                customer,
                new ArrayList<>(cart.getItems()),
                LocalDateTime.now(),
                total
        );

        orderStore.put(order.getId(), order);
        cartService.clearCart(customerId);

        LOGGER.log(Level.INFO, "Order created: {0} for customer {1}", new Object[]{order.getId(), customerId});
        return order;
    }

    public List<Order> getOrdersByCustomerId(Integer customerId) {
        customerService.getCustomerById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        List<Order> orders = new ArrayList<>();
        for (Order order : orderStore.values()) {
            if (order.getCustomer().getId() == customerId) {
 
                orders.add(order);
            }
        }
        return orders;
    }

    public Order getOrderById(Integer customerId, Integer orderId) {
        customerService.getCustomerById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        Order order = orderStore.get(orderId);

        if (order == null || order.getCustomer().getId() != customerId) {
            throw new InvalidInputException("Order not found for customer ID " + customerId + " and order ID " + orderId);
}


        return order;
    }
}

