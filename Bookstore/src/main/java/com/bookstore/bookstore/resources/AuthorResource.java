/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.bookstore.resources;
import com.bookstore.bookstore.model.Author;
import com.bookstore.bookstore.model.Book;
import com.bookstore.bookstore.service.AuthorService;
import com.bookstore.bookstore.service.BookService;
import com.bookstore.bookstore.exception.AuthorNotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 *
 * @author ASUS
 */

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {

    private static final AuthorService authorService = new AuthorService();
    private static final BookService bookService = new BookService();

    // POST /authors
    @POST
    public Response addAuthor(Author author) {
        if (author.getFirstName() == null || author.getLastName() == null) {
            throw new BadRequestException("Author must have a first and last name.");
        }
        Author created = authorService.addAuthor(author);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    // GET /authors
    @GET
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    // GET /authors/{id}
    @GET
    @Path("/{id}")
    public Author getAuthorById(@PathParam("id") Integer id) {
        Author author = authorService.getAuthorById(id);
        if (author == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " not found.");
        }
        return author;
    }

    // PUT /authors/{id}
    @PUT
    @Path("/{id}")
    public Author updateAuthor(@PathParam("id") Integer id, Author author) {
        Author updated = authorService.updateAuthor(id, author);
        if (updated == null) {
            throw new AuthorNotFoundException("Cannot update. Author with ID " + id + " not found.");
        }
        return updated;
    }

    // DELETE /authors/{id}
    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") Integer id) {
        boolean deleted = authorService.deleteAuthor(id);
        if (!deleted) {
            throw new AuthorNotFoundException("Cannot delete. Author with ID " + id + " not found.");
        }
        return Response.noContent().build();
    }

    // GET /authors/{id}/books
    @GET
    @Path("/{id}/books")
    public List<Book> getBooksByAuthor(@PathParam("id") Integer authorId) {
        Author author = authorService.getAuthorById(authorId);
        if (author == null) {
            throw new AuthorNotFoundException("Author with ID " + authorId + " not found.");
        }
        return bookService.getBooksByAuthorId(authorId);
    }
}
