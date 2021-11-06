package com.example.books.service;

import com.example.books.entity.Book;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Setter
@RestController
@RequestMapping("/books")
public class BookController {

    Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService bookService;

    @GetMapping("")
    public ResponseEntity<List<Book>> getAll() {
        logger.debug("Calling all");
        return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getOne(@PathVariable Long id) {
        logger.debug("Calling one {}", id);
        return new ResponseEntity<>(bookService.getBookById(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Book> save(@RequestBody Book newBook) {
        logger.debug("Calling save");
        return new ResponseEntity<>(bookService.saveBook(newBook), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@RequestBody Book newBook, @PathVariable Long id) {
        logger.debug("Calling update {}", id);
        return new ResponseEntity<>(bookService.updateBook(newBook, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        logger.debug("Calling delete {}", id);
        bookService.deleteBook(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}