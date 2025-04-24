/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.bookstore.model;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private int id;
    private Customer customer;
    private List<CartItem> items;
    private LocalDateTime orderDate;
    private double totalAmount;
    private String status;

    // Default constructor
    public Order() {}

    // Full constructor
    public Order(int id, Customer customer, List<CartItem> items, LocalDateTime orderDate, double totalAmount) {
        this.id = id;
        this.customer = customer;
        this.items = items;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status; 
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

