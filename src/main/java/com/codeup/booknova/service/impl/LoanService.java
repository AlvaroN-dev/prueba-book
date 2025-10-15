/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.booknova.service.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.codeup.booknova.domain.Book;
import com.codeup.booknova.domain.Loan;
import com.codeup.booknova.domain.Member;
import com.codeup.booknova.domain.MemberRole;
import com.codeup.booknova.exception.DatabaseException;
import com.codeup.booknova.repository.IBookRepository;
import com.codeup.booknova.repository.ILoanRepository;
import com.codeup.booknova.repository.IMemberRepository;
import com.codeup.booknova.service.ILoanService;
import com.codeup.booknova.util.ValidationUtils;

/**
 * Service class for managing loan operations in the NovaBook system.
 * <p>
 * This service provides comprehensive loan management functionality including
 * book lending, returning, loan tracking, and overdue management.
 * It enforces business rules and validates data before delegating to the repository layer.
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
 * @see ILoanRepository
 * @see IBookRepository
 * @see IMemberRepository
 * @see Loan
 */
public class LoanService implements ILoanService {
    private final ILoanRepository loanRepo;
    private final IBookRepository bookRepo;
    private final IMemberRepository memberRepo;

    // Default loan limits
    private static final int REGULAR_MEMBER_LIMIT = 3;
    private static final int PREMIUM_MEMBER_LIMIT = 5;
    private static final int DEFAULT_LOAN_PERIOD_DAYS = 14;

    /**
     * Constructs a LoanService with the specified repositories.
     *
     * @param loanRepo   the loan repository implementation for data access
     * @param bookRepo   the book repository implementation for data access
     * @param memberRepo the member repository implementation for data access
     * @throws IllegalArgumentException if any repository is null
     */
    public LoanService(ILoanRepository loanRepo, IBookRepository bookRepo, IMemberRepository memberRepo) {
        this.loanRepo = loanRepo;
        this.bookRepo = bookRepo;
        this.memberRepo = memberRepo;
    }

    @Override
    public Loan createLoan(Integer memberId, Integer bookId, Integer loanPeriodDays) throws DatabaseException {
        // Validate input data
        ValidationUtils.validateId(memberId, "Member ID");
        ValidationUtils.validateId(bookId, "Book ID");

        if (loanPeriodDays == null || loanPeriodDays <= 0) {
            throw new DatabaseException("Loan period must be positive");
        }

        // Check if member exists and can borrow
        Optional<Member> memberOpt = memberRepo.findActiveById(memberId);
        if (memberOpt.isEmpty()) {
            throw new DatabaseException("Member not found or inactive with ID: " + memberId);
        }

        // Check if member can borrow more books
        if (!canMemberBorrowMore(memberId)) {
            throw new DatabaseException("Member has reached borrowing limit");
        }

        // Check if book exists and is available
        Optional<Book> bookOpt = bookRepo.findById(bookId);
        if (bookOpt.isEmpty()) {
            throw new DatabaseException("Book not found with ID: " + bookId);
        }

        Book book = bookOpt.get();
        if (!book.isAvailable()) {
            throw new DatabaseException("Book is not available for lending");
        }

        // Create loan
        LocalDate loanDate = LocalDate.now();
        LocalDate dueDate = loanDate.plusDays(loanPeriodDays);

        Loan loan = new Loan(memberId, bookId, loanDate, dueDate);

        // Use transaction-like approach: create loan and decrease stock
        try {
            // Decrease book stock
            bookRepo.decreaseStock(bookId);

            // Create loan record
            return loanRepo.create(loan);
        } catch (DatabaseException e) {
            // In a real transaction, this would be rolled back automatically
            throw new DatabaseException("Failed to create loan: " + e.getMessage(), e);
        }
    }

    @Override
    public Loan createLoan(Integer memberId, Integer bookId) throws DatabaseException {
        return createLoan(memberId, bookId, DEFAULT_LOAN_PERIOD_DAYS);
    }

    @Override
    public void returnBook(Integer loanId) throws DatabaseException {
        ValidationUtils.validateId(loanId, "Loan ID");

        // Find the loan
        Optional<Loan> loanOpt = loanRepo.findById(loanId);
        if (loanOpt.isEmpty()) {
            throw new DatabaseException("Loan not found with ID: " + loanId);
        }

        Loan loan = loanOpt.get();
        if (loan.getReturned()) {
            throw new DatabaseException("Book has already been returned");
        }

        // Use transaction-like approach: mark as returned and increase stock
        try {
            // Mark loan as returned
            loanRepo.markAsReturned(loanId);

            // Increase book stock
            bookRepo.increaseStock(loan.getBookId());
        } catch (DatabaseException e) {
            throw new DatabaseException("Failed to return book: " + e.getMessage(), e);
        }
    }

    @Override
    public void returnBook(Integer memberId, Integer bookId) throws DatabaseException {
        ValidationUtils.validateId(memberId, "Member ID");
        ValidationUtils.validateId(bookId, "Book ID");

        // Find active loan for this member and book
        List<Loan> activeLoans = loanRepo.findActiveLoansByMemberId(memberId);
        Optional<Loan> loanOpt = activeLoans.stream()
                .filter(loan -> loan.getBookId().equals(bookId))
                .findFirst();

        if (loanOpt.isEmpty()) {
            throw new DatabaseException("No active loan found for member " + memberId + " and book " + bookId);
        }

        returnBook(loanOpt.get().getId());
    }

    @Override
    public void extendLoan(Integer loanId, Integer additionalDays) throws DatabaseException {
        ValidationUtils.validateId(loanId, "Loan ID");

        if (additionalDays == null || additionalDays <= 0) {
            throw new DatabaseException("Additional days must be positive");
        }

        Optional<Loan> loanOpt = loanRepo.findById(loanId);
        if (loanOpt.isEmpty()) {
            throw new DatabaseException("Loan not found with ID: " + loanId);
        }

        Loan loan = loanOpt.get();
        if (loan.getReturned()) {
            throw new DatabaseException("Cannot extend a returned loan");
        }

        // Extend due date
        LocalDate newDueDate = loan.getDateDue().plusDays(additionalDays);
        loan.setDateDue(newDueDate);

        loanRepo.update(loan);
    }

    @Override
    public Optional<Loan> findLoanById(Integer id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        return loanRepo.findById(id);
    }

    @Override
    public List<Loan> getLoansByMember(Integer memberId) {
        if (memberId == null || memberId <= 0) {
            return List.of();
        }
        return loanRepo.findByMemberId(memberId);
    }

    @Override
    public List<Loan> getActiveLoansByMember(Integer memberId) {
        if (memberId == null || memberId <= 0) {
            return List.of();
        }
        return loanRepo.findActiveLoansByMemberId(memberId);
    }

    @Override
    public List<Loan> getLoansByBook(Integer bookId) {
        if (bookId == null || bookId <= 0) {
            return List.of();
        }
        return loanRepo.findByBookId(bookId);
    }

    @Override
    public List<Loan> getOverdueLoans() {
        return loanRepo.findOverdueLoans();
    }

    @Override
    public List<Loan> getLoansDueOnDate(LocalDate dueDate) {
        if (dueDate == null) {
            return List.of();
        }
        return loanRepo.findLoansDueOnDate(dueDate);
    }

    @Override
    public List<Loan> getLoansDueToday() {
        return getLoansDueOnDate(LocalDate.now());
    }

    @Override
    public List<Loan> getLoansByDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return List.of();
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        return loanRepo.findLoansByDateRange(startDate, endDate);
    }

    @Override
    public List<Loan> getAllActiveLoans() {
        return loanRepo.findAllActiveLoans();
    }

    @Override
    public List<Loan> getAllLoans() {
        return loanRepo.findAll();
    }

    @Override
    public boolean hasActiveLoans(Integer memberId) {
        if (memberId == null || memberId <= 0) {
            return false;
        }
        return loanRepo.hasActiveLoans(memberId);
    }

    @Override
    public int getActiveLoanCount(Integer memberId) {
        if (memberId == null || memberId <= 0) {
            return 0;
        }
        return loanRepo.countActiveLoansByMember(memberId);
    }

    @Override
    public boolean canMemberBorrowMore(Integer memberId) {
        if (memberId == null || memberId <= 0) {
            return false;
        }

        int currentLoans = getActiveLoanCount(memberId);
        int limit = getMemberLoanLimit(memberId);

        return currentLoans < limit;
    }

    @Override
    public int getMemberLoanLimit(Integer memberId) {
        if (memberId == null || memberId <= 0) {
            return 0;
        }

        Optional<Member> memberOpt = memberRepo.findById(memberId);
        if (memberOpt.isEmpty()) {
            return 0;
        }

        Member member = memberOpt.get();
        return member.getRole() == MemberRole.PREMIUM ? PREMIUM_MEMBER_LIMIT : REGULAR_MEMBER_LIMIT;
    }

    /**
     * Calcula la multa por un préstamo retrasado.
     * <p>
     * La multa se calcula a $0.50 por día de retraso. Si no hay retraso o el préstamo
     * no ha sido devuelto, se devuelve 0.0. Un préstamo se considera a tiempo si se
     * devuelve el mismo día o antes de la fecha de vencimiento.
     * </p>
     *
     * @param loanId el ID del préstamo para calcular la multa
     * @return el monto de la multa en dólares
     * @throws DatabaseException si el préstamo no se encuentra o ocurre un error
     */
    public double calculateFine(Integer loanId) throws DatabaseException {
        ValidationUtils.validateId(loanId, "Loan ID");

        Optional<Loan> loanOpt = loanRepo.findById(loanId);
        if (loanOpt.isEmpty()) {
            throw new DatabaseException("Préstamo no encontrado con ID: " + loanId);
        }

        Loan loan = loanOpt.get();
        if (loan.getReturnDate() == null || loan.getDateDue() == null) {
            return 0.0; // No hay multa si no se ha devuelto o la fecha de vencimiento no está definida
        }

        // Solo hay multa si la devolución es después de la fecha de vencimiento
        if (!loan.getReturnDate().isAfter(loan.getDateDue())) {
            return 0.0; // No hay multa si se devuelve a tiempo o antes
        }

        long daysOverdue = java.time.temporal.ChronoUnit.DAYS.between(loan.getDateDue(), loan.getReturnDate());
        if (daysOverdue <= 0) {
            return 0.0; // Asegura que no haya multa si no hay días de retraso
        }

        return daysOverdue * 0.50; // $0.50 por día de retraso
    }

    /**
     * Exporta los préstamos vencidos a un archivo CSV.
     *
     * @param filePath la ruta del archivo CSV a generar
     * @throws IOException si ocurre un error al escribir el archivo
     */
    public void exportOverdueLoansToCSV(String filePath) throws IOException {
        List<Loan> overdueLoans = getOverdueLoans();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("ID Préstamo,ID Miembro,ID Libro,Fecha Préstamo,Fecha Vencimiento\n"); // Cabecera
            for (Loan loan : overdueLoans) {
                writer.write(loan.getId() + "," + loan.getMemberId() + "," + loan.getBookId() + "," + loan.getDateLoaned() + "," + loan.getDateDue() + "\n");
            }
        }
    }

}