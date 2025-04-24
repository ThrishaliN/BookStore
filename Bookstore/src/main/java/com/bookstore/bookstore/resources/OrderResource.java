/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.bookstore.resources;

/**
 *
 * @author ASUS
 */
import com.bookstore.bookstore.model.Order;
import com.bookstore.bookstore.service.OrderService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    @Inject
    private OrderService orderService;

    private static final Logger LOGGER = Logger.getLogger(OrderResource.class.getName());

    // POST /customers/{customerId}/orders
    @POST
    public Response createOrder(@PathParam("customerId") Integer customerId, @Context UriInfo uriInfo) {
        LOGGER.log(Level.INFO, "Creating order for customer ID: {0}", customerId);

        Order order = orderService.createOrder(customerId);

        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(String.valueOf(order.getId()));
        return Response.created(builder.build()).entity(order).build();
    }

    // GET /customers/{customerId}/orders
    @GET
    public Response getOrders(@PathParam("customerId") Integer customerId) {
        LOGGER.log(Level.INFO, "Retrieving all orders for customer ID: {0}", customerId);

        List<Order> orders = orderService.getOrdersByCustomerId(customerId);
        return Response.ok(orders).build();
    }

    // GET /customers/{customerId}/orders/{orderId}
    @GET
    @Path("/{orderId}")
    public Response getOrderById(@PathParam("customerId") Integer customerId, @PathParam("orderId") Integer orderId) {
        LOGGER.log(Level.INFO, "Retrieving order ID: {0} for customer ID: {1}", new Object[]{orderId, customerId});

        Order order = orderService.getOrderById(customerId, orderId);
        return Response.ok(order).build();
    }
}

