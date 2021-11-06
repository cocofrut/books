package com.example.books.exceptions;


public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(Long id) {
        super("Could not find book by id: " + id);
    }
}
