/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.bookstore.exeption.mappers;

/**
 *
 * @author ASUS
 */
import com.bookstore.bookstore.exception.CartNotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.json.Json;
import javax.json.JsonObject;

@Provider
public class CartNotFoundExceptionMapper implements ExceptionMapper<CartNotFoundException> {

    @Override
    public Response toResponse(CartNotFoundException ex) {
        JsonObject error = Json.createObjectBuilder()
            .add("error", "Cart Not Found")
            .add("message", ex.getMessage())
            .build();

        return Response.status(Response.Status.NOT_FOUND).entity(error).build();
    }
}

