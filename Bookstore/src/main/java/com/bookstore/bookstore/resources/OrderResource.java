/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.bookstore.resources;

/**
 *
 * @author ASUS
 */
import com.bookstore.bookstore.exception.CartNotFoundException;
import com.bookstore.bookstore.exception.CustomerNotFoundException;
import com.bookstore.bookstore.exception.InvalidInputException;
import com.bookstore.bookstore.model.Order;
import com.bookstore.bookstore.service.OrderService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    private static final Logger logger = Logger.getLogger(OrderResource.class.getName());
    private final OrderService orderService = new OrderService();

    // POST /customers/{customerId}/orders
    @POST
    public Response placeOrder(@PathParam("customerId") int customerId) {
        validateCustomerId(customerId);

        Order order = orderService.placeOrder(customerId);
        logger.log(Level.INFO, "Order placed successfully. Order ID: {0}", order.getId());
        return Response.status(Response.Status.CREATED).entity(order).build();
    }

    // GET /customers/{customerId}/orders
    @GET
    public Response getAllOrders(@PathParam("customerId") int customerId) {
        validateCustomerId(customerId);

        List<Order> orders = orderService.getAllOrders();
        List<Order> customerOrders = orders.stream()
                .filter(order -> order.getCustomer().getId() == customerId)
                .toList();

        logger.log(Level.INFO, "Fetched {0} orders for customer ID: {1}", new Object[]{customerOrders.size(), customerId});
        return Response.ok(customerOrders).build();
    }

    
    @GET
    @Path("/{orderId}")
    public Response getOrderById(@PathParam("customerId") int customerId, @PathParam("orderId") int orderId) {
        validateCustomerId(customerId);

        if (orderId <= 0) {
            throw new InvalidInputException("Order ID must be a positive number.");
        }

        Optional<Order> optionalOrder = orderService.getOrderById(orderId);

        if (optionalOrder.isEmpty()) {
            logger.log(Level.WARNING, "Order ID {0} not found.", orderId);
            throw new CartNotFoundException("Order not found."); // You don't have OrderNotFoundException, using CartNotFoundException
        }

        Order order = optionalOrder.get();

        if (order.getCustomer().getId() != customerId) {
            logger.log(Level.WARNING, "Order does not belong to customer ID: {0}", customerId);
            throw new InvalidInputException("You are not allowed to access this order.");
        }

        logger.log(Level.INFO, "Fetched order ID: {0} for customer ID: {1}", new Object[]{orderId, customerId});
        return Response.ok(order).build();
    }

    // Helper method to validate customer ID
    private void validateCustomerId(int customerId) {
        if (customerId <= 0) {
            throw new InvalidInputException("Customer ID must be a positive number.");
        }

        boolean customerExists = orderService.doesCustomerExist(customerId); // You must add this method in service
        if (!customerExists) {
            logger.log(Level.WARNING, "Customer ID {0} not found.", customerId);
            throw new CustomerNotFoundException("Customer not found with ID: " + customerId);
        }
    }
}


