package com.finance.service;

import com.finance.exception.InvalidTransactionException;
import com.finance.model.Transaction;
import com.finance.model.TransactionType;
import com.finance.util.FileHandler;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransactionService {

    private final FileHandler fileHandler;

    private List<Transaction> transactions;

    public TransactionService() {
        this.transactions = new ArrayList<>();
        this.fileHandler = new FileHandler();
    }

    public void addTransaction(Transaction transaction) {
        if(transaction.getAmount() <= 0) throw new InvalidTransactionException("Invalid amount!");
        this.transactions.add(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return this.transactions;
    }

    public void deleteTransaction(String id) {
        boolean result = this.transactions.removeIf(transaction -> transaction.getId().equals(id));
        if (!result) throw new InvalidTransactionException("ID was not found!");
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

    public void writeToCSV() {
        fileHandler.writeFile(transactions, this.createCSVHeader());
    }

    public void readFromCSV() {
        this.transactions = fileHandler.readFile("transactions.csv");
    }

    public String createCSVHeader() {
        return String.join(",", Arrays.stream(Transaction.class.getDeclaredFields()).map(Field::getName).toList());
    }
}
