package com.example.books.service;

import com.example.books.entity.Book;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@AutoConfigureMockMvc
public class BookControllerTests {

    @Autowired
    BookService bookService;

    @InjectMocks
    BookController bookController;

    @InjectMocks
    BookControllerExceptionHandler bookControllerExceptionHandler;

    @Autowired
    private MockMvc mvc;

    private static ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        bookController.setBookService(bookService);

        mvc = MockMvcBuilders.standaloneSetup(bookController)
                .setControllerAdvice(bookControllerExceptionHandler)
                .build();
    }

    @Test
    public void getAllBooksTest() throws Exception {
        mvc.perform(get("/books"))
                .andExpect(status().isOk())
        .andExpect(jsonPath("$", Matchers.hasSize(3)));
    }

    @Test
    public void getBookByIdTest() throws Exception {
        mvc.perform(get("/books/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Harry Potter and the Deathly Hallows")));
    }

    @Test
    public void getMissingBookByIdTest() throws Exception {
        mvc.perform(get("/books/200"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void saveBookTest() throws Exception {
        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)));

        Book newBook = new Book();
        newBook.setTitle("One Day");
        newBook.setAuthor("David Nicholls");
        newBook.setGenre("General & Literary Fiction");

        String json = mapper.writeValueAsString(newBook);

        mvc.perform(post("/books")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)
        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.equalTo(4)))
                .andExpect(jsonPath("$.title", is("One Day")));

        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(4)));
    }

    @Test
    public void saveBookWithMissingMandatoryFieldsTest() throws Exception {
        Book newBook = new Book();
        newBook.setAuthor("David Nicholls");

        String json = mapper.writeValueAsString(newBook);

        mvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void updateBook() throws Exception {
        mvc.perform(get("/books/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.genre", is(IsNull.nullValue())));

        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)));

        Book newBook = new Book();
        newBook.setTitle("Harry Potter and the Deathly Hallows");
        newBook.setAuthor("J. K. Rowling");
        newBook.setGenre("Children's Fiction");

        String json = mapper.writeValueAsString(newBook);

        mvc.perform(put("/books/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(2)))
                .andExpect(jsonPath("$.genre", is("Children's Fiction")));

        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)));
    }

    @Test
    public void updateBookWithWrongId() throws Exception {
        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)));

        Book newBook = new Book();
        newBook.setTitle("Harry Potter and the Deathly Hallows");
        newBook.setAuthor("J. K. Rowling");
        newBook.setGenre("Children's Fiction");

        String json = mapper.writeValueAsString(newBook);

        mvc.perform(put("/books/4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(4)))
                .andExpect(jsonPath("$.genre", is("Children's Fiction")));

        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(4)));
    }

    @Test
    public void deleteBook() throws Exception {
        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)));

        mvc.perform(delete("/books/1"))
                .andExpect(status().isOk());

        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }

    @Test
    public void deleteBookWithWrongId() throws Exception {
        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)));

        mvc.perform(delete("/books/1000"))
                .andExpect(status().isNotFound());

        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)));
    }
}
