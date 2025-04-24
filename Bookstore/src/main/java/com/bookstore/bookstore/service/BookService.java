/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.bookstore.service;
import com.bookstore.bookstore.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
/**
 *
 * @author ASUS
 */
public class BookService {

    private static final List<Book> books = new ArrayList<>();
    private static final AtomicInteger idCounter = new AtomicInteger(1);

    // Add a new book
    public Book addBook(Book book) {
        book.setId(idCounter.getAndIncrement());
        books.add(book);
        return book;
    }

    // Get all books
    public List<Book> getAllBooks() {
        return books;
    }

    // Get a book by ID
    public Optional<Book> getBookById(int id) {
        return books.stream()
           .filter(book -> Objects.equals(book.getId(), id))
           .findFirst();
}


    // Update a book by ID
    public Book updateBook(Integer id, Book updatedBook) {
        for (Book book : books) {
            if (Objects.equals(book.getId(), id)) {
                book.setTitle(updatedBook.getTitle());
                book.setAuthorId(updatedBook.getAuthorId());
                book.setIsbn(updatedBook.getIsbn());
                book.setPublicationYear(updatedBook.getPublicationYear());
                book.setPrice(updatedBook.getPrice());
                book.setStock(updatedBook.getStock());
                return book;
            }
        }
        return null; // let resource throw exception
    }

    // Delete a book by ID
    public boolean deleteBook(Integer id) {
        return books.removeIf(book -> Objects.equals(book.getId(), id));
    }
    // Get all books by a specific author ID
    public List<Book> getBooksByAuthorId(Integer authorId) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (Objects.equals(book.getAuthorId(), authorId)) {
                result.add(book);
            }
        }
        return result;
    }
}

