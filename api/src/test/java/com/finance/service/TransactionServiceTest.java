package com.finance.service;

import com.finance.dto.TransactionRequest;
import com.finance.dto.TransactionResponse;
import com.finance.exception.InvalidTransactionException;
import com.finance.model.Category;
import com.finance.model.Transaction;
import com.finance.model.TransactionType;
import com.finance.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void shouldReturnPaginatedTransactions() {
        //1. Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Transaction mockEntity = new Transaction(UUID.randomUUID(), "Coffee", new BigDecimal("5.50"),
                TransactionType.EXPENSE, Category.FOOD, LocalDate.now());
        Page<Transaction> mockPage = new PageImpl<>(List.of(mockEntity));

        when(transactionRepository.findAll(pageable)).thenReturn(mockPage);

        //2. Act
        Page<TransactionResponse> result = transactionService.getAllTransactions(pageable);

        //3. Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("Coffee", result.getContent().get(0).description());

        verify(transactionRepository).findAll(pageable);
    }

    @Test
    void shouldReturnMultipleTransactionsWithinDateRange() {
        // 1. Arrange - Setup dates and Pageable
        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate end = LocalDate.of(2026, 1, 31);
        Pageable pageable = PageRequest.of(0, 10);

        // Create a list of mock entities
        Transaction t1 = new Transaction(UUID.randomUUID(), "Rent", new BigDecimal("1200.00"),
                TransactionType.EXPENSE, Category.OTHER, LocalDate.of(2026, 1, 1));
        Transaction t2 = new Transaction(UUID.randomUUID(), "Salary", new BigDecimal("3000.00"),
                TransactionType.INCOME, Category.OTHER, LocalDate.of(2026, 1, 15));

        List<Transaction> transactions = List.of(t1, t2);
        Page<Transaction> mockPage = new PageImpl<>(transactions, pageable, transactions.size());

        // Mock the repository call
        when(transactionRepository.findAllByDateBetween(pageable, start, end))
                .thenReturn(mockPage);

        // 2. Act
        Page<TransactionResponse> result = transactionService.getByDateRange(pageable, start, end);

        // 3. Assert
        assertEquals(2, result.getContent().size(), "Should have returned 2 transactions");
        assertEquals("Rent", result.getContent().get(0).description());
        assertEquals("Salary", result.getContent().get(1).description());

        // Verify mapping consistency
        assertEquals(new BigDecimal("1200.00"), result.getContent().get(0).amount());
        assertEquals("INCOME", result.getContent().get(1).type());

        verify(transactionRepository).findAllByDateBetween(pageable, start, end);
    }

    @Test
    void shouldReturnThreeExpenses() {

        //1. Arrange
        Transaction e1 = new Transaction(UUID.randomUUID(), "Grocery Store", new BigDecimal("85.50"),
                TransactionType.EXPENSE, Category.FOOD, LocalDate.of(2026, 3, 5));
        Transaction e2 = new Transaction(UUID.randomUUID(), "Internet Bill", new BigDecimal("60.00"),
                TransactionType.EXPENSE, Category.OTHER, LocalDate.of(2026, 3, 10));
        Transaction e3 = new Transaction(UUID.randomUUID(), "Gas Station", new BigDecimal("45.00"),
                TransactionType.EXPENSE, Category.TRANSPORT, LocalDate.of(2026, 3, 15));


        List<Transaction> transactions = List.of(e1, e2, e3);

        // Mock the repository call
        when(transactionRepository.findTop3ByTypeOrderByAmountDesc(TransactionType.EXPENSE))
                .thenReturn(transactions);

        // 2. Act
        List<TransactionResponse> result = transactionService.getTopThreeExpenses();

        // 3. Assert
        assertEquals("Grocery Store", result.get(0).description());
        assertEquals("Internet Bill", result.get(1).description());
        assertEquals("Gas Station", result.get(2).description());

        verify(transactionRepository).findTop3ByTypeOrderByAmountDesc(TransactionType.EXPENSE);
    }

    @Test
    void shouldReturnBalance() {
        //1. Arrange
        when(transactionRepository.sumByType(TransactionType.INCOME))
                .thenReturn(Optional.of(BigDecimal.valueOf(1000)));

        when(transactionRepository.sumByType(TransactionType.EXPENSE))
                .thenReturn(Optional.of(BigDecimal.valueOf(500)));
        //2. Act
        BigDecimal result = transactionService.getBalance();

        //3. Assert
        assertEquals(BigDecimal.valueOf(500), result);

        verify(transactionRepository).sumByType(TransactionType.INCOME);
        verify(transactionRepository).sumByType(TransactionType.EXPENSE);
    }

    @Test
    void shouldCreateTransaction() {
        //1. Arrange

        Transaction transaction = new Transaction(UUID.randomUUID(), "test", BigDecimal.ZERO, TransactionType.EXPENSE, Category.OTHER, LocalDate.now());
        TransactionRequest transactionRequest = new TransactionRequest("test", BigDecimal.ZERO, "Expense", "Other", LocalDate.now());

        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        //2. Act
        TransactionResponse transactionResponse = transactionService.addTransaction(transactionRequest);

        //3. Assert
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());


        Transaction captured = transactionCaptor.getValue();
        assertEquals("test", captured.getDescription());
        assertEquals(TransactionType.EXPENSE, captured.getType());

        assertNotNull(transactionResponse.id());
    }

    @Test
    void shouldDeleteTransaction() {
        //1. Arrange
        UUID id = UUID.randomUUID();

        when(transactionRepository.existsById(id)).thenReturn(true);
        doNothing().when(transactionRepository).deleteById(id);

        //2. Act
        transactionService.deleteTransaction(id);

        //3. Assert
        verify(transactionRepository).existsById(id);
        verify(transactionRepository, times(1)).deleteById(id);

    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentTransaction() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(transactionRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidTransactionException.class,
                () -> transactionService.deleteTransaction(id));

        verify(transactionRepository, never()).deleteById(any());
    }
}
