package com.finance;

import com.finance.model.Category;
import com.finance.model.Transaction;
import com.finance.model.TransactionType;
import com.finance.service.TransactionService;
import com.finance.ui.ConsoleUI;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        TransactionService transactionService = new TransactionService();

        transactionService.addTransaction(new Transaction("Expense", 3000.0, TransactionType.EXPENSE, Category.FOOD, LocalDate.of(2026, 1, 15)));
        transactionService.addTransaction(new Transaction("Expense", 100000.0, TransactionType.EXPENSE, Category.FOOD, LocalDate.of(2026, 2, 15)));
        transactionService.addTransaction(new Transaction("Expense", 21000.0, TransactionType.EXPENSE, Category.FOOD, LocalDate.of(2026, 3, 15)));
        transactionService.addTransaction(new Transaction("Expense", 5000.0, TransactionType.EXPENSE, Category.FOOD, LocalDate.of(2026, 4, 15)));
        transactionService.addTransaction(new Transaction("Expense", 3000.0, TransactionType.EXPENSE, Category.FOOD, LocalDate.of(2026, 5, 15)));

        ConsoleUI consoleUI = new ConsoleUI(transactionService);

        consoleUI.start();
    }
}