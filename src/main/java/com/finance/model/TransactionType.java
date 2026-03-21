package com.finance.model;

public enum TransactionType {
    INCOME,
    EXPENSE;

    public static TransactionType getType(String typeName) {
        if(INCOME.name().toLowerCase().equals(typeName)) {
            return INCOME;
        }
        return EXPENSE;
    }
}
