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
import com.bookstore.bookstore.exception.InvalidInputException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ASUS
 */

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {

    private static final Logger LOGGER = Logger.getLogger(AuthorResource.class.getName());
    private static final AuthorService authorService = new AuthorService();
    private static final BookService bookService = new BookService();

    // POST /authors
    @POST
    public Response addAuthor(Author author) {
        LOGGER.log(Level.INFO, "Adding a new author: {0}", author);
        validateAuthor(author);
        Author created = authorService.addAuthor(author);
        LOGGER.log(Level.INFO, "Author created successfully: {0}", created);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    // GET /authors
    @GET
    public List<Author> getAllAuthors() {
        LOGGER.info("Fetching all authors");
        return authorService.getAllAuthors();
    }

    // GET /authors/{id}
    @GET
    @Path("/{id}")
    public Author getAuthorById(@PathParam("id") Integer id) {
        LOGGER.log(Level.INFO, "Fetching author with ID: {0}", id);
        Author author = authorService.getAuthorById(id);
        if (author == null) {
            LOGGER.log(Level.WARNING, "Author not found with ID: {0}", id);
            throw new AuthorNotFoundException("Author with ID " + id + " not found.");
        }
        return author;
    }

    // PUT /authors/{id}
    @PUT
    @Path("/{id}")
    public Author updateAuthor(@PathParam("id") Integer id, Author author) {
        LOGGER.log(Level.INFO, "Updating author with ID: {0}", id);
        validateAuthor(author);
        Author updated = authorService.updateAuthor(id, author);
        if (updated == null) {
            LOGGER.log(Level.WARNING, "Cannot update. Author not found with ID: {0}", id);
            throw new AuthorNotFoundException("Cannot update. Author with ID " + id + " not found.");
        }
        LOGGER.log(Level.INFO, "Author updated successfully: {0}", updated);
        return updated;
    }

    // DELETE /authors/{id}
    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") Integer id) {
        LOGGER.log(Level.INFO, "Deleting author with ID: {0}", id);
        boolean deleted = authorService.deleteAuthor(id);
        if (!deleted) {
            LOGGER.log(Level.WARNING, "Cannot delete. Author not found with ID: {0}", id);
            throw new AuthorNotFoundException("Cannot delete. Author with ID " + id + " not found.");
        }
        LOGGER.log(Level.INFO, "Author deleted successfully with ID: {0}", id);
        String successMessage = "{\"message\": \"Author with ID " + id + " successfully deleted.\"}";
        return Response.ok(successMessage).build();
    }

    // GET /authors/{id}/books
    @GET
    @Path("/{id}/books")
    public List<Book> getBooksByAuthor(@PathParam("id") Integer authorId) {
        LOGGER.log(Level.INFO, "Fetching books for author ID: {0}", authorId);
        Author author = authorService.getAuthorById(authorId);
        if (author == null) {
            LOGGER.log(Level.WARNING, "Author not found with ID: {0}", authorId);
            throw new AuthorNotFoundException("Author with ID " + authorId + " not found.");
        }
        return bookService.getBooksByAuthorId(authorId);
    }

    // ===== VALIDATION METHOD =====
    private void validateAuthor(Author author) {
        LOGGER.log(Level.INFO, "Validating author details: {0}", author);
        
        if (author.getFirstName() == null || author.getFirstName().trim().isEmpty()) {
            LOGGER.warning("First name is missing.");
            throw new InvalidInputException("First name is required and cannot be empty.");
        }
        if (!author.getFirstName().matches("[a-zA-Z ]+")) {
            LOGGER.log(Level.WARNING, "Invalid first name: {0}", author.getFirstName());
            throw new InvalidInputException("Invalid input: First name must contain only letters and spaces.");
        }
        if (author.getFirstName().trim().length() < 2) {
            LOGGER.log(Level.WARNING, "First name too short: {0}", author.getFirstName());
            throw new InvalidInputException("First name must be at least 2 characters long.");
        }

        if (author.getLastName() == null || author.getLastName().trim().isEmpty()) {
            LOGGER.warning("Last name is missing.");
            throw new InvalidInputException("Last name is required and cannot be empty.");
        }
        if (!author.getLastName().matches("[a-zA-Z ]+")) {
            LOGGER.log(Level.WARNING, "Invalid last name: {0}", author.getLastName());
            throw new InvalidInputException("Invalid input: Last name must contain only letters and spaces.");
        }
        if (author.getLastName().trim().length() < 2) {
            LOGGER.log(Level.WARNING, "Last name too short: {0}", author.getLastName());
            throw new InvalidInputException("Last name must be at least 2 characters long.");
        }

        if (author.getBiography() != null && !author.getBiography().trim().isEmpty()) {
            if (author.getBiography().trim().length() < 10) {
                LOGGER.warning("Biography too short.");
                throw new InvalidInputException("Biography must be at least 10 characters long if provided.");
            }
            if (author.getBiography().trim().length() > 1000) {
                LOGGER.warning("Biography too long.");
                throw new InvalidInputException("Biography must not exceed 1000 characters.");
            }
        }
    }
}


