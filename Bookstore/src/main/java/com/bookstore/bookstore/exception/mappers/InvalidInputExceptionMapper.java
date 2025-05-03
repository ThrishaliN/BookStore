/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.bookstore.exeption.mappers;

/**
 *
 * @author ASUS
 */

import com.bookstore.bookstore.exception.InvalidInputException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidInputExceptionMapper implements ExceptionMapper<InvalidInputException> {

    @Override
    public Response toResponse(InvalidInputException exception) {
        // Return a 400 Bad Request response with the exception message in JSON format
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"message\": \"" + exception.getMessage() + "\"}")
                .type("application/json")
                .build();
    }
}


