package com.codeup.booknova.service;

import com.codeup.booknova.domain.Loan;
import com.codeup.booknova.exception.DatabaseException;
import com.codeup.booknova.repository.IBookRepository;
import com.codeup.booknova.repository.ILoanRepository;
import com.codeup.booknova.repository.IMemberRepository;
import com.codeup.booknova.service.impl.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoanServiceTest {
    @Mock
    private ILoanRepository loanRepo;

    @Mock
    private IBookRepository bookRepo;

    @Mock
    private IMemberRepository memberRepo;

    @InjectMocks
    private LoanService loanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void calculateFine_OnTime_ReturnsZero() {
        Loan loan = new Loan();
        loan.setDateDue(LocalDate.now());
        loan.setReturnDate(LocalDate.now());
        when(loanRepo.findById(1)).thenReturn(Optional.of(loan));

        double fine = loanService.calculateFine(1);
        assertEquals(0.0, fine, "There should be no penalty for returning items on time.");
    }

    @Test
    void calculateFine_Overdue_ReturnsFine() {
        Loan loan = new Loan();
        loan.setDateDue(LocalDate.now().minusDays(5));
        loan.setReturnDate(LocalDate.now()); // Devuelto hoy
        when(loanRepo.findById(2)).thenReturn(Optional.of(loan));

        double fine = loanService.calculateFine(2);
        assertEquals(2.50, fine, "The fine should be $0.50 per day for 5 days.");
    }

    @Test
    void calculateFine_NullReturnDate_ReturnsZero() {
        Loan loan = new Loan();
        loan.setDateDue(LocalDate.now().minusDays(5));
        loan.setReturnDate(null); // No devuelto
        when(loanRepo.findById(3)).thenReturn(Optional.of(loan));

        double fine = loanService.calculateFine(3);
        assertEquals(0.0, fine, "There should be no fine if it has not been returned.");
    }

    @Test
    void calculateFine_NullDueDate_ReturnsZero() {
        Loan loan = new Loan();
        loan.setDateDue(null); // Sin fecha de vencimiento
        loan.setReturnDate(LocalDate.now());
        when(loanRepo.findById(4)).thenReturn(Optional.of(loan));

        double fine = loanService.calculateFine(4);
        assertEquals(0.0, fine, "There should be no fine if there is no expiration date.");
    }

    @Test
    void calculateFine_NonExistentLoan_ThrowsDatabaseException() {
        when(loanRepo.findById(999)).thenReturn(Optional.empty());

        assertThrows(DatabaseException.class, () -> loanService.calculateFine(999));
    }
}
