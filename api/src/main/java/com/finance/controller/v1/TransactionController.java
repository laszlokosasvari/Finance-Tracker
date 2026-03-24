package com.finance.controller.v1;

import com.finance.dto.TransactionRequest;
import com.finance.dto.TransactionResponse;
import com.finance.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @Operation(summary = "Create a new transaction", description = "Saves a transaction and returns the created entity with its UUID")
    public ResponseEntity<TransactionResponse> create(@Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.addTransaction(request));
    }

    @GetMapping
    @Operation(summary = "Get all transactions", description = "Returns a paginated list of all transactions")
    public ResponseEntity<Page<TransactionResponse>> getAll(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(transactionService.getAllTransactions(pageable));
    }

    @DeleteMapping
    @Operation(summary = "Delete a transaction", description = "Removes a transaction from the database by its UUID")
    public ResponseEntity<Void> delete(@RequestParam UUID id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/balance")
    @Operation(summary = "Get total balance", description = "Calculates the sum of all incomes minus expenses")
    public ResponseEntity<BigDecimal> getBalance() {
        return ResponseEntity.ok(transactionService.getBalance());
    }

    @GetMapping("/top-expenses")
    @Operation(summary = "Get top 3 expenses", description = "Returns the three largest expense transactions")
    public ResponseEntity<List<TransactionResponse>> getTopExpenses() {
        return ResponseEntity.ok(transactionService.getTopThreeExpenses());
    }

    @Operation(summary = "Get transactions in date range")
    @GetMapping("/between")
    public ResponseEntity<Page<TransactionResponse>> getTransactionsBetween(
            @ParameterObject Pageable pageable,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate after,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate until
    ) {
        return ResponseEntity.ok(transactionService.getByDateRange(pageable, after, until));
    }
}
