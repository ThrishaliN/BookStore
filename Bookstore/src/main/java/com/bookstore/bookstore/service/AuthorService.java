/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.bookstore.service;
import com.bookstore.bookstore.model.Author;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author ASUS
 */
public class AuthorService {

    private static final List<Author> authors = new ArrayList<>();
    private static final AtomicInteger idCounter = new AtomicInteger(1);

    // Add a new author
    public Author addAuthor(Author author) {
        author.setId(idCounter.getAndIncrement());
        authors.add(author);
        return author;
    }

    // Get all authors
    public List<Author> getAllAuthors() {
        return new ArrayList<>(authors);
    }

    // Get author by ID
    public Author getAuthorById(Integer id) {
        Optional<Author> match = authors.stream()
                .filter(author -> Objects.equals(author.getId(), id))
                .findFirst();
        return match.orElse(null);
    }

    // Update an existing author
    public Author updateAuthor(Integer id, Author updatedAuthor) {
        for (int i = 0; i < authors.size(); i++) {
            Author current = authors.get(i);
           if (Objects.equals(current.getId(), id)) {
                current.setFirstName(updatedAuthor.getFirstName());
                current.setLastName(updatedAuthor.getLastName());
                current.setBiography(updatedAuthor.getBiography());
                return current;
            }
        }
        return null;
    }

    // Delete an author
    public boolean deleteAuthor(Integer id) {
        return authors.removeIf(author -> Objects.equals(author.getId(), id));
    }
}

