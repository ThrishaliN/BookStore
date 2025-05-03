/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.bookstore.exeption.mappers;

/**
 *
 * @author ASUS
 */
import com.bookstore.bookstore.exception.OutOfStockException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.json.Json;
import javax.json.JsonObject;

@Provider
public class OutOfStockExceptionMapper implements ExceptionMapper<OutOfStockException> {

    @Override
    public Response toResponse(OutOfStockException ex) {
        JsonObject error = Json.createObjectBuilder()
            .add("error", "Out of Stock")
            .add("message", ex.getMessage())
            .build();

        return Response.status(Response.Status.CONFLICT).entity(error).build();
    }
}
