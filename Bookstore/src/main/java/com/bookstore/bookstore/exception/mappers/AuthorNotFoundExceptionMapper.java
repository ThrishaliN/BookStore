/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.bookstore.exception.mappers;

/**
 *
 * @author ASUS
 */
import com.bookstore.bookstore.exception.AuthorNotFoundException;


import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;

@Provider
public class AuthorNotFoundExceptionMapper implements ExceptionMapper<AuthorNotFoundException> {

    @Override
    public Response toResponse(AuthorNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "author Not Found");
        errorResponse.put("message", ex.getMessage());

        return Response.status(Response.Status.NOT_FOUND)
                       .entity(errorResponse)
                       .type(MediaType.APPLICATION_JSON)
                       .build();
    }
}



