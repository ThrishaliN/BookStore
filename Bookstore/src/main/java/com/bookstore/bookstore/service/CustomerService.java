/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.bookstore.service;

/**
 *
 * @author ASUS
 */
import com.bookstore.bookstore.model.Customer;
import com.bookstore.bookstore.exception.CustomerNotFoundException;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class CustomerService {

    private final Map<Integer, Customer> customerDB = new HashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    // Create a customer
    public Customer createCustomer(Customer customer) {
        int id = idCounter.getAndIncrement();
        customer.setId(id);
        customerDB.put(id, customer);
        return customer;
    }

    // Retrieve all customers
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customerDB.values());
    }

    // Retrieve customer by ID
    public Optional<Customer> getCustomerById(Integer id) {
        return Optional.ofNullable(customerDB.get(id));
    }

    // Update customer
    public Customer updateCustomer(Integer id, Customer updatedCustomer) {
        if (!customerDB.containsKey(id)) {
            throw new CustomerNotFoundException(id);
        }
        updatedCustomer.setId(id);
        customerDB.put(id, updatedCustomer);
        return updatedCustomer;
    }

    // Delete customer
    public void deleteCustomer(Integer id) {
        if (customerDB.remove(id) == null) {
            throw new CustomerNotFoundException(id);
        }
    }
}

