package com.finance.ui;

import com.finance.model.Category;
import com.finance.model.Transaction;
import com.finance.model.TransactionType;
import com.finance.service.TransactionService;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * 1 → ask the user for transaction details and call addTransaction
 * 2 → call getAllTransactions and print each one
 * 3 → call getBalance and print it
 * 4 → ask for an id and call deleteTransaction
 * 5 → print "Goodbye!" and exit the loop
 */

public class ConsoleUI {
    private final TransactionService transactionService;
    private final Scanner scanner;

    public ConsoleUI(TransactionService transactionService) {
        this.transactionService = transactionService;
        this.scanner = new Scanner(System.in);
    }

    public short callMenu() {
        System.out.println("Welcome to CLI finance tracker!");
        System.out.println("1 → Create a transaction");
        System.out.println("2 → Check all transactions");
        System.out.println("3 → Check Balance");
        System.out.println("4 → Delete transaction");
        System.out.println("5 → Check transactions between dates");
        System.out.println("6 → Check TOP 3 Expense");
        System.out.println("7 → Save Transactions");
        System.out.println("8 → Read Transactions");
        System.out.println("9 → Exit");

        return this.scanner.nextShort();
    }

    public void start() {
        short choice = callMenu();

        while(choice != 9) {
            switch (choice) {
                case 1:
                    createTransaction();
                    break;
                case 2:
                    checkAllTransactions();
                    break;
                case 3:
                    checkBalance();
                    break;
                case 4:
                    deleteTransaction();
                    break;
                case 5:
                    callDateRange();
                    break;
                case 6:
                    topThreeCall();
                    break;
                case 7:
                    callSave();
                    break;
                case 8:
                    callRead();
                    break;
            }
            choice = callMenu();
        }

        System.out.println("Goodbye!");
        this.scanner.close();
    }

    public void createTransaction() {
        String description = "None";
        double amount = 0;
        LocalDate date = LocalDate.now();
        String tmpType = "";
        String tmpCategory  = "";


        System.out.println("Amount: ");
        amount = this.scanner.nextDouble();
        System.out.print("Description: ");
        this.scanner.nextLine();
        description = this.scanner.nextLine();
        System.out.print("Type: ");
        tmpType = this.scanner.nextLine();
        System.out.print("Category: ");
        tmpCategory = this.scanner.nextLine();

        TransactionType type = TransactionType.getType(tmpType);
        Category category = Category.getCategory(tmpCategory);

        try {
            Transaction transaction = new Transaction(description, amount, type, category, date);
            this.transactionService.addTransaction(transaction);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Transaction added!");
    }

    public void checkAllTransactions() {
        System.out.println(transactionService.getAllTransactions());
    }

    public void checkBalance() {
        System.out.println(transactionService.getBalance());
    }

    public void deleteTransaction() {
        System.out.print("Transaction ID: ");
        this.scanner.nextLine();
        String id = this.scanner.nextLine();

        try {
            transactionService.deleteTransaction(id);
            System.out.println("Transaction removed!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void callDateRange() {
        this.scanner.nextLine();
        System.out.println("From: ");
        LocalDate from = LocalDate.parse(this.scanner.nextLine());
        System.out.println("To: ");
        LocalDate to = LocalDate.parse(this.scanner.nextLine());
        System.out.println(this.transactionService.getByDateRange(from, to));

    }

    public void topThreeCall() {
        System.out.println("Top 3 Expenses: ");
        System.out.println(this.transactionService.topThreeExpenses());
    }

    public void callSave() {
        this.transactionService.writeToCSV();
    }

    public void callRead() {
        this.transactionService.readFromCSV();
    }
}