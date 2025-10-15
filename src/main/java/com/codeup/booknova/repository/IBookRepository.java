/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.codeup.booknova.repository;

import java.util.List;
import java.util.Optional;

import com.codeup.booknova.domain.Book;
import com.codeup.booknova.exception.DatabaseException;

/**
 * Repository interface for Book entity data access operations.
 * <p>
 * This interface defines the contract for all book-related database operations
 * in the NovaBook system. It provides methods for book CRUD operations,
 * stock management, and book search functionality.
 * </p>
 * <p>Key operations:</p>
 * <ul>
 * <li>Book creation and management</li>
 * <li>Book searches by various criteria</li>
 * <li>Stock management for lending operations</li>
 * <li>ISBN uniqueness validation</li>
 * </ul>
 * @version 1.0
 * @since 1.0
 * @see Book
 */
public interface IBookRepository {
    
    /**
     * Creates a new book in the system.
     * 
     * @param book the book entity to create
     * @return the created book with generated ID
     * @throws DatabaseException if book data validation fails or database error occurs
     */
    Book create(Book book) throws DatabaseException;
    
    /**
     * Updates an existing book.
     * 
     * @param book the book object with updated information
     * @return the updated book
     * @throws DatabaseException if update fails
     */
    Book update(Book book) throws DatabaseException;
    
    /**
     * Finds a book by ID.
     * 
     * @param id the book ID to search for
     * @return an {@link Optional} containing the book if found, empty otherwise
     */
    Optional<Book> findById(Integer id);
    
    /**
     * Finds a book by ISBN.
     * 
     * @param isbn the ISBN to search for
     * @return an {@link Optional} containing the book if found, empty otherwise
     */
    Optional<Book> findByIsbn(String isbn);
    
    /**
     * Finds books by title (case-insensitive partial match).
     * 
     * @param title the title to search for
     * @return a list of books matching the title, may be empty but never null
     */
    List<Book> findByTitle(String title);
    
    /**
     * Finds books by author (case-insensitive partial match).
     * 
     * @param author the author to search for
     * @return a list of books by the author, may be empty but never null
     */
    List<Book> findByAuthor(String author);
    
    /**
     * Finds all available books (stock > 0).
     * 
     * @return a list of available books, may be empty but never null
     */
    List<Book> findAvailableBooks();
    
    /**
     * Retrieves all books in the system.
     * 
     * @return a list of all books, may be empty but never null
     */
    List<Book> findAll();
    
    /**
     * Checks if a book exists with the given ISBN.
     * 
     * @param isbn the ISBN to check
     * @return {@code true} if a book exists with the ISBN, {@code false} otherwise
     */
    boolean bookExists(String isbn);
    
    /**
     * Updates the stock quantity for a book.
     * 
     * @param bookId the ID of the book to update
     * @param newStock the new stock quantity
     * @throws DatabaseException if update fails
     */
    void updateStock(Integer bookId, Integer newStock) throws DatabaseException;
    
    /**
     * Decreases the stock by one (for lending).
     * 
     * @param bookId the ID of the book
     * @throws DatabaseException if update fails or book not available
     */
    void decreaseStock(Integer bookId) throws DatabaseException;
    
    /**
     * Increases the stock by one (for returning).
     * 
     * @param bookId the ID of the book
     * @throws DatabaseException if update fails
     */
    void increaseStock(Integer bookId) throws DatabaseException;
    
    /**
     * Deletes a book from the database.
     * 
     * @param id the ID of the book to delete
     * @throws DatabaseException if deletion fails
     */
    void delete(Integer id) throws DatabaseException;
}