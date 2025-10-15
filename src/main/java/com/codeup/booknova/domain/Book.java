/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.booknova.domain;

import java.time.Instant;

/**
 * Book entity representing a book in the NovaBook library system.
 * <p>
 * This class encapsulates all book-related information including
 * identification, authorship, stock management, and timestamps.
 * </p>
 * <p>Example usage:</p>
 * <pre>{@code
 * Book book = new Book("978-3-16-148410-0", "The Great Gatsby", "F. Scott Fitzgerald", 5);
 * book.setStock(10);
 * }</pre>
 * @version 1.0
 * @since 1.0
 */
public class Book {
    private Integer id;
    private String isbn;
    private String title;
    private String author;
    private Integer stock;
    private Instant createdAt;
    private Instant updatedAt;

    /**
     * Constructs a new Book with the specified details.
     * <p>
     * Creates a book instance with ISBN, title, author, and initial stock.
     * </p>
     * 
     * @param isbn the ISBN (International Standard Book Number)
     * @param title the title of the book
     * @param author the author of the book
     * @param stock the initial stock quantity
     * @throws IllegalArgumentException if any parameter is null, empty, or stock is negative
     */
    public Book(String isbn, String title, String author, Integer stock) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty");
        }
        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("Stock cannot be null or negative");
        }
        
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.stock = stock;
    }

    /**
     * Default constructor for Book.
     */
    public Book() {
        this.stock = 0;
    }

    @Override
    public String toString() {
        return "Book{id=%s, isbn='%s', title='%s', author='%s', stock=%s, createdAt=%s, updatedAt=%s}"
            .formatted(id, isbn, title, author, stock, createdAt, updatedAt);
    }

    /**
     * Checks if the book is available for loan.
     * @return true if stock is greater than 0, false otherwise
     */
    public boolean isAvailable() {
        return stock != null && stock > 0;
    }

    /**
     * Decreases the stock by one unit (for lending).
     * @throws IllegalStateException if no stock is available
     */
    public void lendOut() {
        if (!isAvailable()) {
            throw new IllegalStateException("No stock available for lending");
        }
        this.stock--;
    }

    /**
     * Increases the stock by one unit (for returning).
     */
    public void returnBook() {
        this.stock++;
    }

    /**
     * Gets the unique identifier of the book.
     * @return the book ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the book.
     * @param id the book ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the ISBN of the book.
     * @return the ISBN
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Sets the ISBN of the book.
     * @param isbn the ISBN
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Gets the title of the book.
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the book.
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the author of the book.
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author of the book.
     * @param author the author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets the current stock quantity.
     * @return the stock quantity
     */
    public Integer getStock() {
        return stock;
    }

    /**
     * Sets the stock quantity.
     * @param stock the stock quantity
     */
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    /**
     * Gets the creation timestamp.
     * @return the creation timestamp
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp.
     * @param createdAt the creation timestamp
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the last update timestamp.
     * @return the last update timestamp
     */
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the last update timestamp.
     * @param updatedAt the last update timestamp
     */
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}