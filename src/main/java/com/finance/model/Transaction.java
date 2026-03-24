package com.finance.model;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

/**
 * id — String
 * description — String
 * amount — double
 * type — TransactionType
 * category — Category
 * date — LocalDate
 */

public class Transaction {
    private String id;
    private String description;
    private Double amount;
    private TransactionType type;
    private Category category;
    private LocalDate date;

    public Transaction(String description, double amount, TransactionType type, Category category, LocalDate date) {
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.date = date;
    }

    public Transaction(String id, String description, double amount, TransactionType type, Category category, LocalDate date) {
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", type=" + type +
                ", category=" + category +
                ", date=" + date +
                '}';
    }

    public String createCSVLine() {
        String csvLine = String.join(",", this.id, this.description, this.amount.toString(), this.type.name(), this.category.name(), this.date.toString());
        System.out.println(csvLine);
        return csvLine;
    }

}
