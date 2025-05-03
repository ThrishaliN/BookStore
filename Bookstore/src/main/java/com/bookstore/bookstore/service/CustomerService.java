/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.bookstore.service;

import com.bookstore.bookstore.model.Customer;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomerService {

    private static final Map<Integer, Customer> customers = new HashMap<>();
    private static final AtomicInteger idCounter = new AtomicInteger(1);

    public Customer addCustomer(Customer customer) {
        int id = idCounter.getAndIncrement();
        customer.setId(id);
        customers.put(id, customer);
        return customer;
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }

    public Optional<Customer> getCustomerById(int id) {
        return Optional.ofNullable(customers.get(id));
    }

    public Optional<Customer> updateCustomer(int id, Customer updatedCustomerData) {
    if (updatedCustomerData == null) {
        System.out.println("updateCustomer called with null customer data!");
        return Optional.empty();
    }

    return Optional.ofNullable(customers.get(id))
            .map(existingCustomer -> {
                updatedCustomerData.setId(id);
                customers.put(id, updatedCustomerData);
                return updatedCustomerData;
            });
}

    public boolean deleteCustomer(int id) {
        return customers.remove(id) != null;
    }
}
