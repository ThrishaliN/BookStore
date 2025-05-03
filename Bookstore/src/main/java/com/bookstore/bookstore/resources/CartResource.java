/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.bookstore.resources;


import com.bookstore.bookstore.model.Cart;
import com.bookstore.bookstore.model.CartItem;
import com.bookstore.bookstore.service.CartService;
import com.bookstore.bookstore.exception.InvalidInputException;
import java.util.Map;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {

    private final CartService cartService = new CartService();
    private static final Logger logger = Logger.getLogger(CartResource.class.getName());

   
    @POST
    @Path("/items")
    public Response addItem(@PathParam("customerId") int customerId, Map<String, Object> requestBody) {
        Object bookIdObj = requestBody.get("bookId");
        Object quantityObj = requestBody.get("quantity");

        if (bookIdObj == null || quantityObj == null) {
            throw new InvalidInputException("Invalid input: bookId and quantity are required.");
        }

        int bookId, quantity;
        try {
            bookId = Integer.parseInt(bookIdObj.toString());
            quantity = Integer.parseInt(quantityObj.toString());
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid input: bookId and quantity must be numbers.");
        }

        if (bookId <= 0 || quantity <= 0) {
            throw new InvalidInputException("Invalid input: bookId and quantity must be positive numbers.");
        }

        // Now safely create CartItem
        CartItem item = new CartItem();
        item.setBookId(bookId);
        item.setQuantity(quantity);

        // Add to cart
        Cart cart = cartService.addItemToCart(customerId, item);
        logger.log(Level.INFO, "Item added to cart for customer {0}", customerId);

        return Response.status(Response.Status.CREATED).entity(cart).build();
    }



    @GET
    
    
    public Response getCart(@PathParam("customerId") int customerId) {
        Cart cart = cartService.getCart(customerId);
        logger.log(Level.INFO, "Retrieved cart for customer {0}", customerId);
        return Response.ok(cart).build();
    }



    @PUT
    @Path("/items/{bookId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateQuantity(@PathParam("customerId") int customerId,
                                @PathParam("bookId") int bookId,
                                Map<String, Object> requestBody) {
        Object quantityObj = requestBody.get("quantity");

        if (quantityObj == null) {
            throw new InvalidInputException("Quantity field is missing.");
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityObj.toString());
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid input: Quantity must be a number.");
        }

        if (quantity <= 0) {
            throw new InvalidInputException("Invalid input: Quantity must be greater than zero.");
        }

        // Now quantity is safely parsed
        Cart cart = cartService.updateItemQuantity(customerId, bookId, quantity);
        logger.log(Level.INFO, "Updated item quantity for customer {0}, book {1}", new Object[]{customerId, bookId});

        return Response.ok(cart).build();
    }




    @DELETE
    @Path("/items/{bookId}")
    public Response deleteItem(@PathParam("customerId") String customerIdStr,
                            @PathParam("bookId") String bookIdStr) {
        int customerId = parseId(customerIdStr, "Customer ID");
        int bookId = parseId(bookIdStr, "Book ID");

        // Remove the item from the cart
        cartService.removeItem(customerId, bookId);
        logger.log(Level.INFO, "Removed item from cart for customer {0}, book {1}", new Object[]{customerId, bookId});

        String successMessage = "{\"message\": \"Item deleted successfully.\"}";
        return Response.ok(successMessage).build();
    }


    private int parseId(String idStr, String fieldName) {
        try {
            return Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid input: " + fieldName + " must be a number.");
        }
    }


} 



