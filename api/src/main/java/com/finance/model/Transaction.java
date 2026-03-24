package com.finance.model;

import com.finance.dto.TransactionResponse;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "description")
    private String description;

    @Column(name = "amount")
    private BigDecimal amount;

    @NotNull(message = "Type must be provided")
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TransactionType type;

    @NotNull(message = "Category must be provided")
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @NotNull(message = "Date of transaction must be provided")
    @Column(name = "date")
    private LocalDate date;

    public TransactionResponse mapToResponseDTO() {
        return new TransactionResponse(
                this.id.toString(),
                this.description,
                this.amount,
                this.type.name(),
                this.category.name(),
                this.date
        );
    }



}
