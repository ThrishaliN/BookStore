/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.bookstore.resources;

import com.bookstore.bookstore.model.Book;
import com.bookstore.bookstore.service.BookService;
import com.bookstore.bookstore.service.AuthorService;
import com.bookstore.bookstore.model.Author;
import com.bookstore.bookstore.exception.BookNotFoundException;

import java.time.Year;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    private static final Logger logger = Logger.getLogger(BookResource.class.getName());

    private final BookService bookService = new BookService();
    private final AuthorService authorService = new AuthorService();

    @POST
    public Response addBook(Book book) {
        int currentYear = Year.now().getValue();

        // Basic validation
        if (book.getTitle() == null || book.getPrice() <= 0) {
            logger.warning("Invalid input while adding a book: Title missing or price invalid.");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Invalid Input\",\"message\":\"Title must not be null and price must be positive.\"}")
                    .build();
        }
        if (book.getPublicationYear() > currentYear) {
            logger.warning("Invalid input while adding a book: Publication year in the future.");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Invalid Input\",\"message\":\"Publication year cannot be in the future.\"}")
                    .build();
        }

        // Validate if author exists
        Author author = authorService.getAuthorById(book.getAuthorId());
        if (author == null) {
            logger.log(Level.WARNING, "Invalid input while adding a book: Author not found with ID {0}", book.getAuthorId());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Invalid Input\",\"message\":\"Author with ID " + book.getAuthorId() + " not found.\"}")
                    .build();
        }

        // Validate duplicate book
        List<Book> existingBooks = bookService.getAllBooks();
        for (Book existing : existingBooks) {
            if (existing.getTitle().equalsIgnoreCase(book.getTitle()) &&
                existing.getAuthorId().equals(book.getAuthorId())) {
                logger.log(Level.WARNING, "Duplicate book detected: {0} by author ID {1}", new Object[]{book.getTitle(), book.getAuthorId()});
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Invalid Input\",\"message\":\"A book with the same title and author already exists.\"}")
                        .build();
            }
        }

        Book createdBook = bookService.addBook(book);
        logger.log(Level.INFO, "Book added successfully: {0} (ID: {1})", new Object[]{createdBook.getTitle(), createdBook.getId()});
        return Response.status(Response.Status.CREATED).entity(createdBook).build();
    }

    @GET
    public List<Book> getAllBooks() {
        logger.info("Fetching all books.");
        return bookService.getAllBooks();
    }

    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") Integer id) {
        logger.log(Level.INFO, "Fetching book with ID: {0}", id);
        Book book = BookService.getBookById(id);
        if (book == null) {
            logger.log(Level.WARNING, "Book not found with ID: {0}", id);
            throw new BookNotFoundException("Book with ID " + id + " not found.");
        }
        return Response.ok(book).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") Integer id, Book book) {
        int currentYear = Year.now().getValue();

        if (book.getTitle() == null || book.getPrice() <= 0) {
            logger.warning("Invalid input while updating a book: Title missing or price invalid.");
            return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Invalid Input\",\"message\":\"Title must not be null and price must be positive.\"}")
                        .build();
        }
        if (book.getPublicationYear() > currentYear) {
            logger.warning("Invalid input while updating a book: Publication year in the future.");
            return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Invalid Input\",\"message\":\"Publication year cannot be in the future.\"}")
                        .build();
        }

        Book updatedBook = bookService.updateBook(id, book);
        if (updatedBook == null) {
            logger.log(Level.WARNING, "Book not found while updating. ID: {0}", id);
            throw new BookNotFoundException("Book with ID " + id + " not found.");
        }

        logger.log(Level.INFO, "Book updated successfully. ID: {0}", id);
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
        return Response.ok("Book with ID " + id + " has been successfully deleted.").build();
    }

}
