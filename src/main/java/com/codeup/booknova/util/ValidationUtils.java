/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.booknova.util;

import java.util.regex.Pattern;

import com.codeup.booknova.exception.DatabaseException;

/**
 * Comprehensive validation utility class for NovaBook domain objects.
 * <p>
 * This class provides static validation methods for all major domain objects
 * in the NovaBook system including users, books, members, and loans.
 * It enforces business rules and data integrity constraints.
 * </p>
 * 
 * @author tonys-dev
 * @version 1.0
 * @since 1.0
 */
public class ValidationUtils {
    
    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    // Phone validation pattern (digits only, 10-15 digits)
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^\\d{10,15}$"
    );
    
    // ISBN pattern (basic validation)
    private static final Pattern ISBN_PATTERN = Pattern.compile(
        "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$"
    );
    
    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private ValidationUtils() {}
    
    /**
     * Validates user registration data.
     * 
     * @param name the user's name
     * @param email the user's email
     * @param password the user's password
     * @param phone the user's phone number
     * @throws DatabaseException if validation fails
     */
    public static void validateUser(String name, String email, String password, String phone) 
            throws DatabaseException {
        validateName(name);
        validateEmail(email);
        validatePassword(password);
        validatePhone(phone);
    }
    
    /**
     * Validates a name field.
     * 
     * @param name the name to validate
     * @throws DatabaseException if validation fails
     */
    public static void validateName(String name) throws DatabaseException {
        if (name == null || name.trim().isEmpty()) {
            throw new DatabaseException("Name cannot be null or empty");
        }
        if (name.length() > 100) {
            throw new DatabaseException("Name cannot exceed 100 characters");
        }
        if (name.trim().matches("^\\d+$")) {
            throw new DatabaseException("Name cannot contain only numbers");
        }
    }
    
    /**
     * Validates an email address.
     * 
     * @param email the email to validate
     * @throws DatabaseException if validation fails
     */
    public static void validateEmail(String email) throws DatabaseException {
        if (email == null || email.trim().isEmpty()) {
            throw new DatabaseException("Email cannot be null or empty");
        }
        if (email.length() > 120) {
            throw new DatabaseException("Email cannot exceed 120 characters");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new DatabaseException("Invalid email format");
        }
    }
    
    /**
     * Validates a password.
     * 
     * @param password the password to validate
     * @throws DatabaseException if validation fails
     */
    public static void validatePassword(String password) throws DatabaseException {
        if (password == null || password.trim().isEmpty()) {
            throw new DatabaseException("Password cannot be null or empty");
        }
        if (password.length() < 8) {
            throw new DatabaseException("Password must be at least 8 characters long");
        }
        if (password.length() > 255) {
            throw new DatabaseException("Password cannot exceed 255 characters");
        }
    }
    
    /**
     * Validates a phone number.
     * 
     * @param phone the phone number to validate
     * @throws DatabaseException if validation fails
     */
    public static void validatePhone(String phone) throws DatabaseException {
        if (phone == null || phone.trim().isEmpty()) {
            throw new DatabaseException("Phone cannot be null or empty");
        }
        if (phone.length() > 30) {
            throw new DatabaseException("Phone cannot exceed 30 characters");
        }
        // Remove common phone number characters for validation
        String cleanPhone = phone.replaceAll("[\\s\\-\\(\\)\\+]", "");
        if (!PHONE_PATTERN.matcher(cleanPhone).matches()) {
            throw new DatabaseException("Invalid phone number format");
        }
    }
    
    /**
     * Validates book data.
     * 
     * @param isbn the book's ISBN
     * @param title the book's title
     * @param author the book's author
     * @param stock the book's stock
     * @throws DatabaseException if validation fails
     */
    public static void validateBook(String isbn, String title, String author, Integer stock) 
            throws DatabaseException {
        validateIsbn(isbn);
        validateTitle(title);
        validateAuthor(author);
        validateStock(stock);
    }
    
    /**
     * Validates an ISBN.
     * 
     * @param isbn the ISBN to validate
     * @throws DatabaseException if validation fails
     */
    public static void validateIsbn(String isbn) throws DatabaseException {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new DatabaseException("ISBN cannot be null or empty");
        }
        if (isbn.length() > 20) {
            throw new DatabaseException("ISBN cannot exceed 20 characters");
        }
        // Basic ISBN format validation
        String cleanIsbn = isbn.replaceAll("[\\s\\-]", "");
        if (cleanIsbn.length() != 10 && cleanIsbn.length() != 13) {
            throw new DatabaseException("ISBN must be 10 or 13 digits");
        }
    }
    
    /**
     * Validates a book title.
     * 
     * @param title the title to validate
     * @throws DatabaseException if validation fails
     */
    public static void validateTitle(String title) throws DatabaseException {
        if (title == null || title.trim().isEmpty()) {
            throw new DatabaseException("Title cannot be null or empty");
        }
        if (title.length() > 100) {
            throw new DatabaseException("Title cannot exceed 100 characters");
        }
    }
    
    /**
     * Validates an author name.
     * 
     * @param author the author to validate
     * @throws DatabaseException if validation fails
     */
    public static void validateAuthor(String author) throws DatabaseException {
        if (author == null || author.trim().isEmpty()) {
            throw new DatabaseException("Author cannot be null or empty");
        }
        if (author.length() > 100) {
            throw new DatabaseException("Author cannot exceed 100 characters");
        }
    }
    
    /**
     * Validates stock quantity.
     * 
     * @param stock the stock to validate
     * @throws DatabaseException if validation fails
     */
    public static void validateStock(Integer stock) throws DatabaseException {
        if (stock == null) {
            throw new DatabaseException("Stock cannot be null");
        }
        if (stock < 0) {
            throw new DatabaseException("Stock cannot be negative");
        }
    }
    
    /**
     * Validates a positive integer ID.
     * 
     * @param id the ID to validate
     * @param fieldName the name of the field for error messages
     * @throws DatabaseException if validation fails
     */
    public static void validateId(Integer id, String fieldName) throws DatabaseException {
        if (id == null) {
            throw new DatabaseException(fieldName + " cannot be null");
        }
        if (id <= 0) {
            throw new DatabaseException(fieldName + " must be positive");
        }
    }
}