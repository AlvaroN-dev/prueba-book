/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.codeup.booknova.service;

import java.util.List;
import java.util.Optional;

import com.codeup.booknova.domain.Book;
import com.codeup.booknova.exception.DatabaseException;

/**
 * Service interface for Book management operations.
 * <p>
 * This interface defines the contract for all book-related business operations
 * in the NovaBook system. It provides methods for book management, inventory
 * control, and book search functionality.
 * </p>
 * <p>Key responsibilities:</p>
 * <ul>
 * <li>Book registration and catalog management</li>
 * <li>Inventory and stock management</li>
 * <li>Book search and discovery</li>
 * <li>Availability checking for lending operations</li>
 * </ul>
 * @version 1.0
 * @since 1.0
 * @see Book
 * @see com.codeup.booknova.repository.IBookRepository
 */
public interface IBookService {
    
    /**
     * Adds a new book to the library catalog.
     * <p>
     * This method validates the book data and ensures ISBN uniqueness
     * before adding the book to the system.
     * </p>
     * 
     * @param isbn the ISBN of the book, must be unique
     * @param title the title of the book
     * @param author the author of the book
     * @param initialStock the initial stock quantity
     * @return the created book with generated ID
     * @throws DatabaseException if validation fails, ISBN already exists, or database error occurs
     */
    Book addBook(String isbn, String title, String author, Integer initialStock) throws DatabaseException;
    
    /**
     * Updates an existing book's information.
     * 
     * @param book the book with updated information
     * @return the updated book
     * @throws DatabaseException if update fails
     */
    Book updateBook(Book book) throws DatabaseException;
    
    /**
     * Finds a book by its ID.
     * 
     * @param id the book ID
     * @return an Optional containing the book if found
     */
    Optional<Book> findBookById(Integer id);
    
    /**
     * Finds a book by its ISBN.
     * 
     * @param isbn the ISBN to search for
     * @return an Optional containing the book if found
     */
    Optional<Book> findBookByIsbn(String isbn);
    
    /**
     * Searches for books by title (partial match, case-insensitive).
     * 
     * @param title the title to search for
     * @return a list of books matching the title search
     */
    List<Book> searchBooksByTitle(String title);
    
    /**
     * Searches for books by author (partial match, case-insensitive).
     * 
     * @param author the author to search for
     * @return a list of books by the specified author
     */
    List<Book> searchBooksByAuthor(String author);
    
    /**
     * Retrieves all books in the library catalog.
     * 
     * @return a list of all books, may be empty but never null
     */
    List<Book> getAllBooks();
    
    /**
     * Retrieves all available books (stock > 0).
     * 
     * @return a list of available books, may be empty but never null
     */
    List<Book> getAvailableBooks();
    
    /**
     * Checks if a book is available for lending.
     * 
     * @param bookId the ID of the book to check
     * @return {@code true} if the book is available, {@code false} otherwise
     */
    boolean isBookAvailable(Integer bookId);
    
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
     * @param bookId the ID of the book
     * @param newStock the new stock quantity
     * @throws DatabaseException if update fails
     */
    void updateBookStock(Integer bookId, Integer newStock) throws DatabaseException;
    
    /**
     * Increases the stock of a book by a specified amount.
     * 
     * @param bookId the ID of the book
     * @param quantity the quantity to add to stock
     * @throws DatabaseException if update fails
     */
    void addStock(Integer bookId, Integer quantity) throws DatabaseException;
    
    /**
     * Removes a book from the library catalog.
     * <p>
     * This operation should check if the book has any active loans
     * before allowing deletion.
     * </p>
     * 
     * @param bookId the ID of the book to remove
     * @throws DatabaseException if deletion fails or book has active loans
     */
    void removeBook(Integer bookId) throws DatabaseException;
    
    /**
     * Gets the current stock level for a book.
     * 
     * @param bookId the ID of the book
     * @return the current stock quantity, or 0 if book not found
     */
    Integer getBookStock(Integer bookId);
}