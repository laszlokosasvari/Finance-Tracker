package com.finance.model;

import com.finance.exception.InvalidTransactionException;

import java.util.Arrays;

public enum TransactionType {
    INCOME, EXPENSE;

    public static TransactionType getType(String typeName) {
        return Arrays.stream(TransactionType.values())
                .filter(t -> t.name().equalsIgnoreCase(typeName))
                .findFirst()
                .orElseThrow(() -> new InvalidTransactionException("Invalid type: " + typeName));
    }
}
