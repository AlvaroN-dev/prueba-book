package com.codeup.booknova.service;

import com.codeup.booknova.domain.Book;
import com.codeup.booknova.exception.DatabaseException;
import com.codeup.booknova.repository.IBookRepository;
import com.codeup.booknova.service.impl.BookService;
import com.codeup.booknova.util.ValidationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {
    @Mock
    private IBookRepository repo;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validateStock_Null_ThrowsDatabaseException() {
        assertThrows(DatabaseException.class, () -> ValidationUtils.validateStock(null));
    }

    @Test
    void validateStock_Negative_ThrowsDatabaseException() {
        assertThrows(DatabaseException.class, () -> ValidationUtils.validateStock(-1));
    }

    @Test
    void validateStock_ValidValue_DoesNotThrowException() {
        assertDoesNotThrow(() -> ValidationUtils.validateStock(5));
    }

    @Test
    void addBook_ValidInput_CallsRepoCreate() throws DatabaseException {
        Book book = new Book("123-4567890123", "Test Book", "Test Author", 5);
        when(repo.create(any(Book.class))).thenReturn(book);

        Book result = bookService.addBook("123-4567890123", "Test Book", "Test Author", 5);

        assertNotNull(result, "The result should not be null.");
        assertEquals(book, result, "The returned book should be the same one that was created.");
        verify(repo, times(1)).create(any(Book.class)); // Verify that the method is called
    }

    @Test
    void addBook_NullStock_ThrowsDatabaseException() {
        assertThrows(DatabaseException.class, () ->
                bookService.addBook("123-4567890123", "Test Book", "Test Author", null));
    }

    @Test
    void addBook_NegativeStock_ThrowsDatabaseException() {
        assertThrows(DatabaseException.class, () ->
                bookService.addBook("123-4567890123", "Test Book", "Test Author", -1));
    }
}
