package com.example.books.service;

import com.example.books.entity.ErrorResponse;
import com.example.books.exceptions.BookNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class BookControllerExceptionHandler {

    static final String BOOK_NOT_FOUND = "Book not found";
    static final String FAILED_VALIDATION = "Request failed validation";
    static final String MISSING_RESOURCE = "Couldn't find resource";
    static final String INTERNAL_ERROR = "Internal server error occurred\"";

    Logger logger = LoggerFactory.getLogger(BookControllerExceptionHandler.class);

    @ExceptionHandler(BookNotFoundException.class)
    public final ResponseEntity<ErrorResponse> notFoundException(BookNotFoundException bnfe) {
        logger.error(BOOK_NOT_FOUND, bnfe);
        return new ResponseEntity<>(new ErrorResponse(bnfe.getMessage()), HttpStatus.NOT_FOUND); // 404
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<ErrorResponse> constraintViolationException(ConstraintViolationException cve) {
        logger.error(FAILED_VALIDATION, cve);
        return new ResponseEntity<>(new ErrorResponse(FAILED_VALIDATION), HttpStatus.NOT_ACCEPTABLE); // 406
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public final ResponseEntity<ErrorResponse> emptyResultDataAccessException(EmptyResultDataAccessException erde) {
        logger.error(MISSING_RESOURCE, erde);
        return new ResponseEntity<>(new ErrorResponse(MISSING_RESOURCE), HttpStatus.NOT_FOUND); // 404
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        logger.error(INTERNAL_ERROR, ex);
        return new ResponseEntity<>(new ErrorResponse(INTERNAL_ERROR), HttpStatus.INTERNAL_SERVER_ERROR); // 500
    }
}
