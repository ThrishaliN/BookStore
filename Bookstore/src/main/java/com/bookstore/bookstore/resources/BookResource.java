/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.bookstore.resources;

import com.bookstore.bookstore.model.Book;
import com.bookstore.bookstore.service.BookService;
import com.bookstore.bookstore.exception.BookNotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
/**
 *
 * @author ASUS
 */

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    private final BookService bookService = new BookService();

    @POST
    public Response addBook(Book book) {
        // Basic validation
        if (book.getTitle() == null || book.getPrice() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("Invalid book data").build();
}


        Book createdBook = bookService.addBook(book);
        System.out.println("Book added: " + createdBook.getTitle());
        return Response.status(Response.Status.CREATED).entity(createdBook).build();
    }

    @GET
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") Integer id) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + id + " not found.");
        }
        return Response.ok(book).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") Integer id, Book book) {
        // Basic validation
        if (book.getTitle() == null || book.getPrice() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Invalid book data").build();
        }

        Book updatedBook = bookService.updateBook(id, book);
        if (updatedBook == null) {
            throw new BookNotFoundException("Book with ID " + id + " not found.");
        }

        System.out.println("Book updated: ID " + id);
        return Response.ok(updatedBook).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") Integer id) {
        boolean deleted = bookService.deleteBook(id);
        if (!deleted) {
            throw new BookNotFoundException("Book with ID " + id + " not found.");
        }

        System.out.println("Book deleted: ID " + id);
        return Response.noContent().build();
    }
}

