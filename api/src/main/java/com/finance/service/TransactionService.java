package com.finance.service;

import com.finance.dto.TransactionRequest;
import com.finance.dto.TransactionResponse;
import com.finance.exception.InvalidTransactionException;
import com.finance.model.Transaction;
import com.finance.model.TransactionType;
import com.finance.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class TransactionService {

    private TransactionRepository transactionRepository;

    @Transactional
    public TransactionResponse addTransaction(TransactionRequest transactionRequest) {

        Transaction transaction = transactionRequest.mapToEntity();
        log.info("TransactionRequest mapped to entity");
        transaction = this.transactionRepository.save(transaction);
        log.info("Transaction entity saved");
        log.info("Returning TransactionResponse...");
        return transaction.mapToResponseDTO();
    }

    public Page<TransactionResponse> getAllTransactions(Pageable pageable) {
        Page<Transaction> transactions = this.transactionRepository.findAll(pageable);
        log.info("Transactions received, returning transaction page");
        return transactions.map(Transaction::mapToResponseDTO);
    }

    @Transactional
    public void deleteTransaction(UUID id) {
        if(transactionRepository.existsById(id)) {
            log.info("Transaction found, deleting transaction...");
            this.transactionRepository.deleteById(id);
        } else {
            throw new InvalidTransactionException("No transaction with " + id);
        }
    }

    private BigDecimal getTotalIncome() {
        Optional<BigDecimal> totalIncome = this.transactionRepository.sumByType(TransactionType.INCOME);
        log.info("Income transactions summed, returning total income...");
        return totalIncome.orElse(BigDecimal.ZERO);
    }

    private BigDecimal getTotalExpense() {
        Optional<BigDecimal> totalExpense = this.transactionRepository.sumByType(TransactionType.EXPENSE);
        log.info("Expense transactions summed, returning total expense...");
        return totalExpense.orElse(BigDecimal.ZERO);
    }

    public BigDecimal getBalance() {
        log.info("Calculating account balance");
        return getTotalIncome().subtract(getTotalExpense());
    }

    public Page<TransactionResponse> getByDateRange(Pageable pageable, LocalDate from, LocalDate to) {
        log.info("Requesting transactions between {} and {}", from, to);
        Page<Transaction> entities = transactionRepository.findAllByDateBetween(pageable, from, to);
        return entities.map(Transaction::mapToResponseDTO);
    }

    public List<TransactionResponse> getTopThreeExpenses() {
        log.info("Requesting top 3 expenses");
        return transactionRepository.findTop3ByTypeOrderByAmountDesc(TransactionType.EXPENSE)
                .stream()
                .map(Transaction::mapToResponseDTO)
                .toList();
    }
}
