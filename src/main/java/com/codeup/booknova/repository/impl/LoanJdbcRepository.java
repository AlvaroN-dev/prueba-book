/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.booknova.repository.impl;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.codeup.booknova.domain.Loan;
import com.codeup.booknova.exception.DatabaseException;
import com.codeup.booknova.jdbc.JdbcTemplateLight;
import com.codeup.booknova.jdbc.RowMapper;
import com.codeup.booknova.repository.ILoanRepository;
import com.codeup.booknova.util.ValidationUtils;

/**
 * JDBC implementation of the Loan repository interface.
 * 
 * <p>This repository provides data access operations for {@link Loan} entities using
 * JDBC through the {@link JdbcTemplateLight} abstraction. It handles loan transaction
 * management including creation, updates, and various search operations.</p>
 * 
 * <p><strong>Key Features:</strong></p>
 * <ul>
 *   <li><strong>Loan Tracking</strong> - Manages book lending and return transactions</li>
 *   <li><strong>Overdue Management</strong> - Identifies and tracks overdue loans</li>
 *   <li><strong>Search Capabilities</strong> - Supports search by member, book, and date ranges</li>
 *   <li><strong>Status Management</strong> - Tracks active vs returned loans</li>
 * </ul>
 * @version 1.0
 * @since 1.0
 * @see ILoanRepository
 * @see Loan
 * @see JdbcTemplateLight
 * @see ValidationUtils
 */
public class LoanJdbcRepository implements ILoanRepository {
    
    private final JdbcTemplateLight jdbc;
    private static final Logger logger = Logger.getLogger(LoanJdbcRepository.class.getName());

    /**
     * Constructs a new LoanJdbcRepository with the specified JDBC template.
     * 
     * @param jdbc the JDBC template for database operations
     * @throws NullPointerException if jdbc is null
     */
    public LoanJdbcRepository(JdbcTemplateLight jdbc) { 
        this.jdbc = jdbc; 
    }

    // Row MAPPER
    private static final RowMapper<Loan> LOAN_MAPPER = rs -> {
        Loan loan = new Loan(
            rs.getInt("member_id"),
            rs.getInt("book_id"),
            rs.getDate("date_loaned").toLocalDate(),
            rs.getDate("date_due").toLocalDate()
        );
        loan.setId(rs.getInt("id"));
        loan.setReturned(rs.getBoolean("returned"));
        loan.setCreatedAt(rs.getTimestamp("created_at").toInstant());
        loan.setUpdatedAt(rs.getTimestamp("updated_at").toInstant());
        return loan;
    };

    @Override
    public Loan create(Loan loan) throws DatabaseException {
        
        // Validations
        ValidationUtils.validateId(loan.getMemberId(), "Member ID");
        ValidationUtils.validateId(loan.getBookId(), "Book ID");
        
        if (loan.getDateLoaned() == null) {
            throw new DatabaseException("Date loaned cannot be null");
        }
        if (loan.getDateDue() == null) {
            throw new DatabaseException("Date due cannot be null");
        }

        String sql = "INSERT INTO loan (member_id, book_id, date_loaned, date_due, returned) VALUES (?, ?, ?, ?, ?)";
        try {
            int rows = jdbc.update(sql, ps -> {
                try {
                    ps.setInt(1, loan.getMemberId());
                    ps.setInt(2, loan.getBookId());
                    ps.setDate(3, Date.valueOf(loan.getDateLoaned()));
                    ps.setDate(4, Date.valueOf(loan.getDateDue()));
                    ps.setBoolean(5, loan.getReturned() != null ? loan.getReturned() : false);
                } catch (SQLException e) {
                    throw new RuntimeException("Error creating loan", e);
                }
            });
            if (rows != 1) {
                throw new DatabaseException("Failed to create loan");
            }

            // Find the created loan by searching for the most recent loan for this member and book
            List<Loan> loans = findByMemberId(loan.getMemberId());
            return loans.stream()
                    .filter(l -> l.getBookId().equals(loan.getBookId()) && 
                               l.getDateLoaned().equals(loan.getDateLoaned()))
                    .max((l1, l2) -> l1.getId().compareTo(l2.getId()))
                    .orElseThrow(() -> new DatabaseException("Failed to retrieve created loan"));
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error creating loan", e);
            throw e;
        }
    }

    @Override
    public Loan update(Loan loan) throws DatabaseException {
        String sql = "UPDATE loan SET member_id=?, book_id=?, date_loaned=?, date_due=?, returned=? WHERE id=?";
        try {
            int rows = jdbc.update(sql, ps -> {
                try {
                    ps.setInt(1, loan.getMemberId());
                    ps.setInt(2, loan.getBookId());
                    ps.setDate(3, Date.valueOf(loan.getDateLoaned()));
                    ps.setDate(4, Date.valueOf(loan.getDateDue()));
                    ps.setBoolean(5, loan.getReturned());
                    ps.setInt(6, loan.getId());
                } catch (SQLException e) {
                    throw new RuntimeException("Error updating loan", e);
                }
            });
            if (rows != 1) {
                throw new DatabaseException("Failed to update loan");
            }
            return loan;
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error updating loan", e);
            throw e;
        }
    }

    @Override
    public Optional<Loan> findById(Integer id) {
        String sql = "SELECT * FROM loan WHERE id = ?";
        try {
            List<Loan> loans = jdbc.query(sql, ps -> {
                try {
                    ps.setInt(1, id);
                } catch (SQLException e) {
                    throw new RuntimeException("Error setting parameters", e);
                }
            }, LOAN_MAPPER);
            return loans.isEmpty() ? Optional.empty() : Optional.of(loans.get(0));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error finding loan by id", e);
            return Optional.empty();
        }
    }

    @Override
    public List<Loan> findByMemberId(Integer memberId) {
        String sql = "SELECT * FROM loan WHERE member_id = ?";
        try {
            List<Loan> list = jdbc.query(sql, ps -> {
                try { ps.setInt(1, memberId); } catch (SQLException e) { throw new RuntimeException("Error searching loans by member ID", e); }
            }, LOAN_MAPPER);
            logger.log(Level.INFO, "Loan search by member ID executed: {0}", memberId);
            return list;
        } catch (DatabaseException e) { 
            logger.log(Level.SEVERE, "Error executing search by member ID: {0}", e.getMessage());
            throw new RuntimeException("Database error during loan search by member ID", e);
        }
    }

    @Override
    public List<Loan> findByBookId(Integer bookId) {
        String sql = "SELECT * FROM loan WHERE book_id = ?";
        try {
            List<Loan> list = jdbc.query(sql, ps -> {
                try { ps.setInt(1, bookId); } catch (SQLException e) { throw new RuntimeException("Error searching loans by book ID", e); }
            }, LOAN_MAPPER);
            logger.log(Level.INFO, "Loan search by book ID executed: {0}", bookId);
            return list;
        } catch (DatabaseException e) { 
            logger.log(Level.SEVERE, "Error executing search by book ID: {0}", e.getMessage());
            throw new RuntimeException("Database error during loan search by book ID", e);
        }
    }

    @Override
    public List<Loan> findActiveLoansByMemberId(Integer memberId) {
        String sql = "SELECT * FROM loan WHERE member_id = ? AND returned = false";
        try {
            List<Loan> list = jdbc.query(sql, ps -> {
                try { ps.setInt(1, memberId); } catch (SQLException e) { throw new RuntimeException("Error searching active loans by member ID", e); }
            }, LOAN_MAPPER);
            logger.log(Level.INFO, "Active loan search by member ID executed: {0}", memberId);
            return list;
        } catch (DatabaseException e) { 
            logger.log(Level.SEVERE, "Error executing active loan search by member ID: {0}", e.getMessage());
            throw new RuntimeException("Database error during active loan search by member ID", e);
        }
    }

    @Override
    public List<Loan> findActiveLoansByBookId(Integer bookId) {
        String sql = "SELECT * FROM loan WHERE book_id = ? AND returned = false";
        try {
            List<Loan> list = jdbc.query(sql, ps -> {
                try { ps.setInt(1, bookId); } catch (SQLException e) { throw new RuntimeException("Error searching active loans by book ID", e); }
            }, LOAN_MAPPER);
            logger.log(Level.INFO, "Active loan search by book ID executed: {0}", bookId);
            return list;
        } catch (DatabaseException e) { 
            logger.log(Level.SEVERE, "Error executing active loan search by book ID: {0}", e.getMessage());
            throw new RuntimeException("Database error during active loan search by book ID", e);
        }
    }

    @Override
    public List<Loan> findOverdueLoans() {
        String sql = "SELECT * FROM loan WHERE date_due < CURDATE() AND returned = false";
        try {
            logger.log(Level.INFO, "Overdue loans search executed");
            return jdbc.query(sql, null, LOAN_MAPPER);
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error executing overdue loans search: {0}", e.getMessage());
            throw new RuntimeException("Database error during overdue loans search", e);
        }
    }

    @Override
    public List<Loan> findLoansDueOnDate(LocalDate dueDate) {
        String sql = "SELECT * FROM loan WHERE date_due = ? AND returned = false";
        try {
            List<Loan> list = jdbc.query(sql, ps -> {
                try { ps.setDate(1, Date.valueOf(dueDate)); } catch (SQLException e) { throw new RuntimeException("Error searching loans due on date", e); }
            }, LOAN_MAPPER);
            logger.log(Level.INFO, "Loans due on date search executed: {0}", dueDate);
            return list;
        } catch (DatabaseException e) { 
            logger.log(Level.SEVERE, "Error executing loans due on date search: {0}", e.getMessage());
            throw new RuntimeException("Database error during loans due on date search", e);
        }
    }

    @Override
    public List<Loan> findLoansByDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT * FROM loan WHERE date_loaned BETWEEN ? AND ?";
        try {
            List<Loan> list = jdbc.query(sql, ps -> {
                try { 
                    ps.setDate(1, Date.valueOf(startDate)); 
                    ps.setDate(2, Date.valueOf(endDate)); 
                } catch (SQLException e) { throw new RuntimeException("Error searching loans by date range", e); }
            }, LOAN_MAPPER);
            logger.log(Level.INFO, "Loans by date range search executed: {0} to {1}", new Object[]{startDate, endDate});
            return list;
        } catch (DatabaseException e) { 
            logger.log(Level.SEVERE, "Error executing loans by date range search: {0}", e.getMessage());
            throw new RuntimeException("Database error during loans by date range search", e);
        }
    }

    @Override
    public List<Loan> findAll() {
        String sql = "SELECT * FROM loan";
        try {
            logger.log(Level.INFO, "Loan list executed");
            return jdbc.query(sql, null, LOAN_MAPPER);
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error executing list loans: {0}", e.getMessage());
            throw new RuntimeException("Database error during loan list", e);
        }
    }

    @Override
    public List<Loan> findAllActiveLoans() {
        String sql = "SELECT * FROM loan WHERE returned = false";
        try {
            logger.log(Level.INFO, "Active loans list executed");
            return jdbc.query(sql, null, LOAN_MAPPER);
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error executing active loans list: {0}", e.getMessage());
            throw new RuntimeException("Database error during active loans list", e);
        }
    }

    @Override
    public void markAsReturned(Integer loanId) throws DatabaseException {
        String sql = "UPDATE loan SET returned = true WHERE id = ?";
        try {
            int rows = jdbc.update(sql, ps -> {
                try {
                    ps.setInt(1, loanId);
                } catch (SQLException e) {
                    throw new RuntimeException("Error marking loan as returned", e);
                }
            });
            if (rows != 1) {
                throw new DatabaseException("Failed to mark loan as returned - loan may not exist");
            }
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error marking loan as returned", e);
            throw e;
        }
    }

    @Override
    public boolean hasActiveLoans(Integer memberId) {
        String sql = "SELECT COUNT(*) FROM loan WHERE member_id = ? AND returned = false";
        try {
            List<Integer> count = jdbc.query(sql, ps -> {
                try { ps.setInt(1, memberId); } catch (SQLException e) { throw new RuntimeException("Error checking active loans", e); }
            }, rs -> rs.getInt(1));
            return count.stream().findFirst().orElse(0) > 0;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error checking active loans for member", e);
            return false;
        }
    }

    @Override
    public int countActiveLoansByMember(Integer memberId) {
        String sql = "SELECT COUNT(*) FROM loan WHERE member_id = ? AND returned = false";
        try {
            List<Integer> count = jdbc.query(sql, ps -> {
                try { ps.setInt(1, memberId); } catch (SQLException e) { throw new RuntimeException("Error counting active loans", e); }
            }, rs -> rs.getInt(1));
            return count.stream().findFirst().orElse(0);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error counting active loans for member", e);
            return 0;
        }
    }

    @Override
    public void delete(Integer id) throws DatabaseException {
        String sql = "DELETE FROM loan WHERE id = ?";
        try {
            jdbc.update(sql, ps -> {
                try {
                    ps.setInt(1, id);
                } catch (SQLException e) {
                    throw new RuntimeException("Error deleting loan", e);
                }
            });
        } catch (DatabaseException e) {
            logger.log(Level.SEVERE, "Error deleting loan", e);
            throw e;
        }
    }
}