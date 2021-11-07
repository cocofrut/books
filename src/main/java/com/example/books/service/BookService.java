package com.example.books.service;

import com.example.books.entity.Book;
import com.example.books.exceptions.BookNotFoundException;
import com.example.books.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    public Book saveBook(Book newBook) {
        return bookRepository.save(newBook);
    }

    public Book updateBook(Book newBook, Long id) {
        return bookRepository.findById(id)
                .map(oldBook -> {
                    oldBook.setAuthor(newBook.getAuthor());
                    oldBook.setGenre(newBook.getGenre());
                    oldBook.setTitle(newBook.getTitle());
                    return bookRepository.save(oldBook);
                })
                .orElseGet(() -> {
                    newBook.setId(id);
                    return bookRepository.save(newBook);
                });
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
