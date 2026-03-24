package com.finance.dto;

import com.finance.model.Category;
import com.finance.model.Transaction;
import com.finance.model.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(
        @NotBlank(message = "Description is required")
        String description,

        @Positive(message = "Amount must be greater than zero")
        BigDecimal amount,

        @NotNull(message = "Type must be provided")
        String type,

        @NotNull(message = "Category must be provided")
        String category,

        @NotNull(message = "Date of transaction must be provided")
        LocalDate date
) {

    public Transaction mapToEntity() {

        return new Transaction(null, description, amount, TransactionType.getType(type), Category.getCategory(category), date);
    }
}
