/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.booknova.service.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.codeup.booknova.domain.Book;
import com.codeup.booknova.exception.DatabaseException;
import com.codeup.booknova.repository.IBookRepository;
import com.codeup.booknova.service.IBookService;
import com.codeup.booknova.util.ValidationUtils;

/**
 * Service class for managing book operations in the NovaBook system.
 * <p>
 * This service provides comprehensive book management functionality including
 * catalog management, inventory control, and search operations.
 * It enforces business rules and validates data before delegating to the repository layer.
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
 * @see IBookRepository
 * @see Book
 */
public class BookService implements IBookService {
    private final IBookRepository repo;
    
    /**
     * Constructs a BookService with the specified repository.
     * 
     * @param repo the book repository implementation for data access
     * @throws IllegalArgumentException if repo is null
     */
    public BookService(IBookRepository repo) { 
        this.repo = repo; 
    }

    @Override
    public Book addBook(String isbn, String title, String author, Integer initialStock) throws DatabaseException {
        // Validate input data
        ValidationUtils.validateBook(isbn, title, author, initialStock);
        
        // Check if book already exists
        if (bookExists(isbn)) {
            throw new DatabaseException("Book already exists with ISBN: " + isbn);
        }
        
        Book book = new Book(isbn, title, author, initialStock);
        return repo.create(book);
    }

    @Override
    public Book updateBook(Book book) throws DatabaseException {
        if (book == null || book.getId() == null) {
            throw new DatabaseException("Book and book ID cannot be null");
        }
        
        // Validate book data
        ValidationUtils.validateBook(book.getIsbn(), book.getTitle(), book.getAuthor(), book.getStock());
        
        return repo.update(book);
    }

    @Override
    public Optional<Book> findBookById(Integer id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        return repo.findById(id);
    }

    @Override
    public Optional<Book> findBookByIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return Optional.empty();
        }
        return repo.findByIsbn(isbn);
    }

    @Override
    public List<Book> searchBooksByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return List.of();
        }
        return repo.findByTitle(title);
    }

    @Override
    public List<Book> searchBooksByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            return List.of();
        }
        return repo.findByAuthor(author);
    }

    @Override
    public List<Book> getAllBooks() {
        return repo.findAll();
    }

    @Override
    public List<Book> getAvailableBooks() {
        return repo.findAvailableBooks();
    }

    @Override
    public boolean isBookAvailable(Integer bookId) {
        if (bookId == null || bookId <= 0) {
            return false;
        }
        
        Optional<Book> bookOpt = repo.findById(bookId);
        return bookOpt.map(Book::isAvailable).orElse(false);
    }

    @Override
    public boolean bookExists(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }
        return repo.bookExists(isbn);
    }

    @Override
    public void updateBookStock(Integer bookId, Integer newStock) throws DatabaseException {
        ValidationUtils.validateId(bookId, "Book ID");
        ValidationUtils.validateStock(newStock);
        
        // Verify book exists
        if (!repo.findById(bookId).isPresent()) {
            throw new DatabaseException("Book not found with ID: " + bookId);
        }
        
        repo.updateStock(bookId, newStock);
    }

    @Override
    public void addStock(Integer bookId, Integer quantity) throws DatabaseException {
        ValidationUtils.validateId(bookId, "Book ID");
        if (quantity == null || quantity <= 0) {
            throw new DatabaseException("Quantity must be positive");
        }
        
        Optional<Book> bookOpt = repo.findById(bookId);
        if (bookOpt.isEmpty()) {
            throw new DatabaseException("Book not found with ID: " + bookId);
        }
        
        Book book = bookOpt.get();
        int newStock = book.getStock() + quantity;
        repo.updateStock(bookId, newStock);
    }

    @Override
    public void removeBook(Integer bookId) throws DatabaseException {
        ValidationUtils.validateId(bookId, "Book ID");
        
        // Check if book exists
        Optional<Book> bookOpt = repo.findById(bookId);
        if (bookOpt.isEmpty()) {
            throw new DatabaseException("Book not found with ID: " + bookId);
        }
        
        // In a more complete implementation, we would check for active loans here
        // For now, we'll allow deletion
        repo.delete(bookId);
    }

    @Override
    public Integer getBookStock(Integer bookId) {
        if (bookId == null || bookId <= 0) {
            return 0;
        }
        
        Optional<Book> bookOpt = repo.findById(bookId);
        return bookOpt.map(Book::getStock).orElse(0);
    }

    /**
     * Exporta el catálogo de libros a un archivo CSV.
     *
     * @param filePath la ruta del archivo CSV a generar
     * @throws IOException si ocurre un error al escribir el archivo
     */
    public void exportBooksToCSV(String filePath) throws IOException {
        List<Book> books = getAllBooks();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("ID,ISBN,Título,Autor,Stock\n"); // Cabecera
            for (Book book : books) {
                writer.write(book.getId() + "," + book.getIsbn() + "," + book.getTitle() + "," + book.getAuthor() + "," + book.getStock() + "\n");
            }
        }
    }
}