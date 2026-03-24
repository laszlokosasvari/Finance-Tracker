package com.finance;

import com.finance.model.Transaction;
import com.finance.service.TransactionService;
import com.finance.ui.ConsoleUI;

import java.lang.reflect.Field;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        TransactionService transactionService = new TransactionService();

        ConsoleUI consoleUI = new ConsoleUI(transactionService);

        consoleUI.start();

        System.out.println(Arrays.stream(Transaction.class.getDeclaredFields()).map(Field::getName).toList());

    }
}