/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.bookstore.exeption.mappers;

/**
 *
 * @author ASUS
 */
import com.bookstore.bookstore.exception.BookNotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.MediaType;

import java.util.HashMap;
import java.util.Map;

@Provider
public class BookNotFoundExceptionMapper implements ExceptionMapper<BookNotFoundException> {

    @Override
    public Response toResponse(BookNotFoundException exception) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Book Not Found");
        errorResponse.put("message", exception.getMessage());

        return Response.status(Response.Status.NOT_FOUND)
                       .type(MediaType.APPLICATION_JSON)
                       .entity(errorResponse)
                       .build();
    }
}

