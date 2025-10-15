/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.booknova.domain;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Loan entity representing a book loan transaction in the NovaBook library system.
 * <p>
 * This class encapsulates all information related to a book loan including
 * the member, book, loan dates, return status, and associated timestamps.
 * Loans can be in an open state (ongoing) or closed state (returned).
 * </p>
 * <p>Example usage:</p>
 * <pre>{@code
 * Loan loan = new Loan(memberId, bookId, LocalDate.now(), LocalDate.now().plusDays(14));
 * loan.setReturned(false);
 * }</pre>
 * @version 1.0
 * @since 1.0
 * @see Member
 * @see Book
 */
public class Loan {
    private Integer id;
    private Integer memberId;
    private Integer bookId;
    private LocalDate dateLoaned;
    private LocalDate dateDue;
    private Boolean returned;
    private Instant createdAt;
    private Instant updatedAt;
    private LocalDate returnDate;

    /**
     * Constructs a new Loan with the specified details.
     * <p>
     * Creates a loan instance with member, book, and date information.
     * The loan is initially set as not returned (ongoing).
     * </p>
     * 
     * @param memberId the unique identifier of the member making the loan
     * @param bookId the unique identifier of the book being loaned
     * @param dateLoaned the date when the book was loaned
     * @param dateDue the due date for returning the book
     * @throws IllegalArgumentException if any parameter is null or if dateLoaned is after dateDue
     */
    public Loan(Integer memberId, Integer bookId, LocalDate dateLoaned, LocalDate dateDue) {
        if (memberId == null) {
            throw new IllegalArgumentException("Member ID cannot be null");
        }
        if (bookId == null) {
            throw new IllegalArgumentException("Book ID cannot be null");
        }    // fecha de prestamo
        if (dateLoaned == null) {
            throw new IllegalArgumentException("Date loaned cannot be null");
        }   // fecha de vencimiento
        if (dateDue == null) {
            throw new IllegalArgumentException("Date due cannot be null");
        }
        if (dateLoaned.isAfter(dateDue)) {
            throw new IllegalArgumentException("Date loaned cannot be after due date");
        }
        
        this.memberId = memberId;
        this.bookId = bookId;
        this.dateLoaned = dateLoaned;
        this.dateDue = dateDue;
        this.returned = false; // regreso
    }

    /**
     * Default constructor for Loan.
     */
    public Loan() {
        this.returned = false;
    }

    @Override
    public String toString() {
        return "Loan{id=%s, memberId=%s, bookId=%s, dateLoaned=%s, dateDue=%s, returned=%s, createdAt=%s, updatedAt=%s}"
            .formatted(id, memberId, bookId, dateLoaned, dateDue, returned, createdAt, updatedAt);
    }

    /**
     * Checks if the loan is overdue.
     * @return true if the current date is after the due date and the book is not returned
     */
    public boolean isOverdue() {
        if (returned != null && returned) {
            return false; // Already returned, not overdue
        }
        return dateDue != null && LocalDate.now().isAfter(dateDue);
    }

    /**
     * Marks the loan as returned.
     */
    public void markAsReturned() {
        this.returned = true;
    }
                // marcar como devuelto


    /**
     * Gets the number of days until due date (negative if overdue).
     * @return days until due date, negative if overdue
     */
    public long getDaysUntilDue() { // obtener los dias hasta vencer
        if (dateDue == null) {
            return 0;
        }
        return LocalDate.now().until(dateDue).getDays();
    }

    /**
     * Gets the unique identifier of the loan.
     * @return the loan ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the loan.
     * @param id the loan ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the unique identifier of the member.
     * @return the member ID
     */
    public Integer getMemberId() {
        return memberId;
    }

    /**
     * Sets the unique identifier of the member.
     * @param memberId the member ID
     */
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    /**
     * Gets the unique identifier of the book.
     * @return the book ID
     */
    public Integer getBookId() {
        return bookId;
    }

    /**
     * Sets the unique identifier of the book.
     * @param bookId the book ID
     */
    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    /**
     * Gets the date when the book was loaned.
     * @return the loan date
     */
    public LocalDate getDateLoaned() {
        return dateLoaned;
    }

    /**
     * Sets the date when the book was loaned.
     * @param dateLoaned the loan date
     */
    public void setDateLoaned(LocalDate dateLoaned) {
        this.dateLoaned = dateLoaned;
    }

    /**
     * Gets the due date for returning the book.
     * @return the due date
     */
    public LocalDate getDateDue() {
        return dateDue;
    }

    /**
     * Sets the due date for returning the book.
     * @param dateDue the due date
     */
    public void setDateDue(LocalDate dateDue) {
        this.dateDue = dateDue;
    }

    /**
     * Checks if the book has been returned.
     * @return true if returned, false otherwise
     */
    public Boolean getReturned() {
        return returned;
    }

    /**
     * Sets the returned status of the loan.
     * @param returned true if returned, false otherwise
     */
    public void setReturned(Boolean returned) {
        this.returned = returned;
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


    public LocalDate getReturnDate() {
        return returnDate;
    }
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}