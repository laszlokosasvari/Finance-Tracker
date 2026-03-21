package com.finance.service;

import com.finance.model.Transaction;
import com.finance.model.TransactionType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * addTransaction — takes a Transaction and adds it to the list
 * getAllTransactions — returns the full list
 * deleteTransaction — takes an id as a String and removes the matching transaction from the list
 * getTotalIncome — loops through the list and sums up all INCOME transactions
 * getTotalExpenses — loops through the list and sums up all EXPENSE transactions
 * getBalance — returns the difference between total income and total expenses
 */

public class TransactionService {

    private final List<Transaction> transactions;

    public TransactionService() {
        this.transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return this.transactions;
    }

    public void deleteTransaction(String id) {
        this.transactions.removeIf(transaction -> transaction.getId().equals(id));
    }

    public double getTotalIncome() {
        Optional<Double> totalIncome =  this.transactions.stream()
                .filter(transaction -> transaction.getType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(Double::sum);

        if(totalIncome.isEmpty()) {
            return 0;
        }
        return totalIncome.get();
    }

    public double getTotalExpense() {
        Optional<Double> totalExpense =  this.transactions.stream()
                .filter(transaction -> transaction.getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(Double::sum);

        if(totalExpense.isEmpty()) {
            return 0;
        }
        return totalExpense.get();
    }

    public double getBalance() {
        return getTotalIncome() - getTotalExpense();
    }

    public List<Transaction> getByDateRange(LocalDate from, LocalDate to) {
        return transactions.stream()
                .filter(transaction ->

                        transaction.getDate().isAfter(from) &&
                        transaction.getDate().isBefore(to)
                ).toList();
    }

    public List<Transaction> topThreeExpenses() {
        return transactions.stream()
                .filter(transaction -> transaction.getType() == TransactionType.EXPENSE)
                .sorted((t2, t1) -> t1.getAmount().compareTo(t2.getAmount()))
                .limit(3)
                .collect(Collectors.toList());
    }
}
