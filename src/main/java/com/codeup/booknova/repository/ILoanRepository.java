/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.codeup.booknova.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.codeup.booknova.domain.Loan;
import com.codeup.booknova.exception.DatabaseException;

/**
 * Repository interface for Loan entity data access operations.
 * <p>
 * This interface defines the contract for all loan-related database operations
 * in the NovaBook system. It provides methods for loan CRUD operations,
 * loan management, and various loan queries.
 * </p>
 * <p>Key operations:</p>
 * <ul>
 * <li>Creating and managing book loans</li>
 * <li>Returning books and updating loan status</li>
 * <li>Finding loans by member, book, or date criteria</li>
 * <li>Overdue loan tracking</li>
 * <li>Loan history queries</li>
 * </ul>
 * @version 1.0
 * @since 1.0
 * @see Loan
 */
public interface ILoanRepository {
    
    /**
     * Creates a new loan in the system.
     * <p>
     * This operation should typically be executed within a transaction
     * that also updates the book's stock quantity.
     * </p>
     * 
     * @param loan the loan entity to create
     * @return the created loan with generated ID
     * @throws DatabaseException if loan data validation fails or database error occurs
     */
    Loan create(Loan loan) throws DatabaseException;
    
    /**
     * Updates an existing loan.
     * 
     * @param loan the loan object with updated information
     * @return the updated loan
     * @throws DatabaseException if update fails
     */
    Loan update(Loan loan) throws DatabaseException;
    
    /**
     * Finds a loan by ID.
     * 
     * @param id the loan ID to search for
     * @return an {@link Optional} containing the loan if found, empty otherwise
     */
    Optional<Loan> findById(Integer id);
    
    /**
     * Finds all loans for a specific member.
     * 
     * @param memberId the member ID to search for
     * @return a list of loans for the member, may be empty but never null
     */
    List<Loan> findByMemberId(Integer memberId);
    
    /**
     * Finds all loans for a specific book.
     * 
     * @param bookId the book ID to search for
     * @return a list of loans for the book, may be empty but never null
     */
    List<Loan> findByBookId(Integer bookId);
    
    /**
     * Finds all active (unreturned) loans for a specific member.
     * 
     * @param memberId the member ID to search for
     * @return a list of active loans for the member, may be empty but never null
     */
    List<Loan> findActiveLoansByMemberId(Integer memberId);
    
    /**
     * Finds all active (unreturned) loans for a specific book.
     * 
     * @param bookId the book ID to search for
     * @return a list of active loans for the book, may be empty but never null
     */
    List<Loan> findActiveLoansByBookId(Integer bookId);
    
    /**
     * Finds all overdue loans (due date passed and not returned).
     * 
     * @return a list of overdue loans, may be empty but never null
     */
    List<Loan> findOverdueLoans();
    
    /**
     * Finds loans due on a specific date.
     * 
     * @param dueDate the due date to search for
     * @return a list of loans due on the specified date, may be empty but never null
     */
    List<Loan> findLoansDueOnDate(LocalDate dueDate);
    
    /**
     * Finds all loans within a date range.
     * 
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return a list of loans within the date range, may be empty but never null
     */
    List<Loan> findLoansByDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * Retrieves all loans in the system.
     * 
     * @return a list of all loans, may be empty but never null
     */
    List<Loan> findAll();
    
    /**
     * Retrieves all active (unreturned) loans.
     * 
     * @return a list of all active loans, may be empty but never null
     */
    List<Loan> findAllActiveLoans();
    
    /**
     * Marks a loan as returned.
     * <p>
     * This operation should typically be executed within a transaction
     * that also updates the book's stock quantity.
     * </p>
     * 
     * @param loanId the ID of the loan to mark as returned
     * @throws DatabaseException if update fails
     */
    void markAsReturned(Integer loanId) throws DatabaseException;
    
    /**
     * Checks if a member has any active loans.
     * 
     * @param memberId the member ID to check
     * @return {@code true} if the member has active loans, {@code false} otherwise
     */
    boolean hasActiveLoans(Integer memberId);
    
    /**
     * Counts the number of active loans for a member.
     * 
     * @param memberId the member ID to count loans for
     * @return the number of active loans
     */
    int countActiveLoansByMember(Integer memberId);
    
    /**
     * Deletes a loan from the database.
     * 
     * @param id the ID of the loan to delete
     * @throws DatabaseException if deletion fails
     */
    void delete(Integer id) throws DatabaseException;
}