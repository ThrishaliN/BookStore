/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.bookstore.resources;

/**
 *
 * @author ASUS
 */
import com.bookstore.bookstore.model.Cart;
import com.bookstore.bookstore.model.CartItem;
import com.bookstore.bookstore.service.CartService;
import com.bookstore.bookstore.exception.CartNotFoundException;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/customers/{customerId}/cart")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CartResource {

    @Inject
    CartService cartService;

    // POST /customers/{customerId}/cart/items - Add item to cart
    @POST
    @Path("/items")
    public Response addItem(@PathParam("customerId") Integer customerId, CartItem item) {
        try {
            Cart updatedCart = cartService.addItem(customerId, item);
            return Response.status(Response.Status.CREATED).entity(updatedCart).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // GET /customers/{customerId}/cart - Get cart
    @GET
    public Response getCart(@PathParam("customerId") Integer customerId) {
        try {
            Cart cart = cartService.getCart(customerId);
            return Response.ok(cart).build();
        } catch (CartNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    // PUT /customers/{customerId}/cart/items/{bookId} - Update item quantity in cart
    @PUT
    @Path("/items/{bookId}")
    public Response updateItem(@PathParam("customerId") Integer customerId, @PathParam("bookId") Integer bookId, int quantity) {
        try {
            Cart updatedCart = cartService.updateItemQuantity(customerId, bookId, quantity);
            return Response.ok(updatedCart).build();
        } catch (CartNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    // DELETE /customers/{customerId}/cart/items/{bookId} - Remove item from cart
    @DELETE
    @Path("/items/{bookId}")
    public Response removeItem(@PathParam("customerId") Integer customerId, @PathParam("bookId") Integer bookId) {
        try {
            cartService.removeItem(customerId, bookId);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (CartNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}

