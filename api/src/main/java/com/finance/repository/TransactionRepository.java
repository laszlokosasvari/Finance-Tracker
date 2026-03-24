package com.finance.repository;

import com.finance.model.Transaction;
import com.finance.model.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Page<Transaction> findAllByDateBetween(Pageable pageable, LocalDate from, LocalDate to);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = :type")
    Optional<BigDecimal> sumByType(TransactionType type);

    List<Transaction> findTop3ByTypeOrderByAmountDesc(TransactionType type);
}
