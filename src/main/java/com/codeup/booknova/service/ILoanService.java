/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.codeup.booknova.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.codeup.booknova.domain.Loan;
import com.codeup.booknova.exception.DatabaseException;

/**
 * Service interface for Loan management operations.
 * <p>
 * This interface defines the contract for all loan-related business operations
 * in the NovaBook system. It provides methods for book lending, returning,
 * loan tracking, and overdue management.
 * </p>
 * <p>Key responsibilities:</p>
 * <ul>
 * <li>Book lending with availability and eligibility validation</li>
 * <li>Book return processing and stock management</li>
 * <li>Loan history and tracking</li>
 * <li>Overdue loan management</li>
 * <li>Member borrowing limit enforcement</li>
 * </ul>
 * @version 1.0
 * @since 1.0
 * @see Loan
 * @see com.codeup.booknova.repository.ILoanRepository
 */
public interface ILoanService {
    
    /**
     * Creates a new book loan.
     * <p>
     * This method validates that:
     * - The member is eligible to borrow
     * - The book is available
     * - The member hasn't exceeded borrowing limits
     * - Updates book stock and creates loan record
     * </p>
     * 
     * @param memberId the ID of the member borrowing the book
     * @param bookId the ID of the book being borrowed
     * @param loanPeriodDays the number of days for the loan (default: 14)
     * @return the created loan with generated ID
     * @throws DatabaseException if validation fails, book unavailable, or database error occurs
     */
    Loan createLoan(Integer memberId, Integer bookId, Integer loanPeriodDays) throws DatabaseException;
    
    /**
     * Creates a new book loan with default loan period (14 days).
     * 
     * @param memberId the ID of the member borrowing the book
     * @param bookId the ID of the book being borrowed
     * @return the created loan with generated ID
     * @throws DatabaseException if validation fails, book unavailable, or database error occurs
     */
    Loan createLoan(Integer memberId, Integer bookId) throws DatabaseException;
    
    /**
     * Returns a book and closes the loan.
     * <p>
     * This method:
     * - Marks the loan as returned
     * - Updates book stock
     * - Calculates any overdue penalties (if applicable)
     * </p>
     * 
     * @param loanId the ID of the loan to close
     * @throws DatabaseException if loan not found, already returned, or database error occurs
     */
    void returnBook(Integer loanId) throws DatabaseException;
    
    /**
     * Returns a book by member and book IDs.
     * 
     * @param memberId the ID of the member returning the book
     * @param bookId the ID of the book being returned
     * @throws DatabaseException if loan not found, already returned, or database error occurs
     */
    void returnBook(Integer memberId, Integer bookId) throws DatabaseException;
    
    /**
     * Extends the due date of an active loan.
     * 
     * @param loanId the ID of the loan to extend
     * @param additionalDays the number of additional days to extend
     * @throws DatabaseException if loan not found, already returned, or extension not allowed
     */
    void extendLoan(Integer loanId, Integer additionalDays) throws DatabaseException;
    
    /**
     * Finds a loan by its ID.
     * 
     * @param id the loan ID
     * @return an Optional containing the loan if found
     */
    Optional<Loan> findLoanById(Integer id);
    
    /**
     * Retrieves all loans for a specific member.
     * 
     * @param memberId the member ID
     * @return a list of loans for the member, may be empty but never null
     */
    List<Loan> getLoansByMember(Integer memberId);
    
    /**
     * Retrieves all active (unreturned) loans for a specific member.
     * 
     * @param memberId the member ID
     * @return a list of active loans for the member, may be empty but never null
     */
    List<Loan> getActiveLoansByMember(Integer memberId);
    
    /**
     * Retrieves loan history for a specific book.
     * 
     * @param bookId the book ID
     * @return a list of loans for the book, may be empty but never null
     */
    List<Loan> getLoansByBook(Integer bookId);
    
    /**
     * Retrieves all overdue loans in the system.
     * 
     * @return a list of overdue loans, may be empty but never null
     */
    List<Loan> getOverdueLoans();
    
    /**
     * Retrieves loans due on a specific date.
     * 
     * @param dueDate the due date to check
     * @return a list of loans due on the specified date
     */
    List<Loan> getLoansDueOnDate(LocalDate dueDate);
    
    /**
     * Retrieves loans due today.
     * 
     * @return a list of loans due today
     */
    List<Loan> getLoansDueToday();
    
    /**
     * Retrieves all loans within a date range.
     * 
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return a list of loans within the date range
     */
    List<Loan> getLoansByDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * Retrieves all active loans in the system.
     * 
     * @return a list of all active loans, may be empty but never null
     */
    List<Loan> getAllActiveLoans();
    
    /**
     * Retrieves all loans in the system.
     * 
     * @return a list of all loans, may be empty but never null
     */
    List<Loan> getAllLoans();
    
    /**
     * Checks if a member has any active loans.
     * 
     * @param memberId the member ID to check
     * @return {@code true} if the member has active loans, {@code false} otherwise
     */
    boolean hasActiveLoans(Integer memberId);
    
    /**
     * Gets the count of active loans for a member.
     * 
     * @param memberId the member ID
     * @return the number of active loans
     */
    int getActiveLoanCount(Integer memberId);
    
    /**
     * Checks if a member can borrow more books based on current loans.
     * 
     * @param memberId the member ID to check
     * @return {@code true} if the member can borrow more books, {@code false} otherwise
     */
    boolean canMemberBorrowMore(Integer memberId);
    
    /**
     * Gets the maximum number of books a member can borrow simultaneously.
     * 
     * @param memberId the member ID
     * @return the maximum loan limit for the member
     */
    int getMemberLoanLimit(Integer memberId);
}