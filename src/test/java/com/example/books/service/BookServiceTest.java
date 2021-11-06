package com.example.books.service;

import com.example.books.entity.Book;
import com.example.books.exceptions.BookNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Test
    public void getAllBooksTest() {
        List<Book> books = bookService.getAllBooks();
        assertNotNull(books);
    }

    @Test
    public void getBookByIdTest() {
        Book expected = new Book();
        expected.setId(1L);
        expected.setTitle("The Da Vinci Code");
        expected.setAuthor("Dan Brown");
        expected.setGenre("Crime, Thriller & Adventure");

        Book actual = bookService.getBookById(1L);

        assertNotNull(actual);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test(expected = BookNotFoundException.class)
    public void getMissingBookByIdTest() {
        bookService.getBookById(100L);
    }

    @Test
    public void saveBookTest() {
        Book expected = new Book();
        expected.setTitle("One Day");
        expected.setAuthor("David Nicholls");
        expected.setGenre("General & Literary Fiction");

        Book actual = bookService.saveBook(expected);
        assertNotNull(actual);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void updateBookTest() {
        Book newBook = new Book();
        newBook.setTitle("Harry Potter and the Deathly Hallows");
        newBook.setAuthor("J. K. Rowling");
        newBook.setGenre("Children's Fiction");

        Book oldBook = bookService.getBookById(2L);
        assertNull(oldBook.getGenre());

        bookService.updateBook(newBook, 2L);
        Book afterUpdate = bookService.getBookById(2L);
        assertNotNull(afterUpdate.getGenre());

        assertEquals(afterUpdate.getGenre(), newBook.getGenre());
    }

    @Test
    public void updateMissingBookTest() {
        List<Book> beforeUpdate = bookService.getAllBooks();
        assertNotNull(beforeUpdate);

        Book newBook = new Book();
        newBook.setTitle("Atonement");
        newBook.setAuthor("Ian McEwan");
        newBook.setGenre("General & Literary Fiction");

        Book oldBook = bookService.getBookById(2L);
        assertNull(oldBook.getGenre());

        bookService.updateBook(newBook, 200L);

        List<Book> afterUpdate = bookService.getAllBooks();

        assertNotEquals(beforeUpdate.size(), afterUpdate.size());
    }

    @Test
    public void deleteBookTest() {
        List<Book> beforeDelete = bookService.getAllBooks();
        assertNotNull(beforeDelete);

        bookService.deleteBook(1L);

        List<Book> afterDelete = bookService.getAllBooks();

        assertNotEquals(beforeDelete.size(), afterDelete.size());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void deleteMissingBookTest() {
        bookService.deleteBook(300L);
    }
}
