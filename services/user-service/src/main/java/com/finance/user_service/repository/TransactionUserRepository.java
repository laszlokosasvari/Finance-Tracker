package com.finance.user_service.repository;

import com.finance.user_service.model.TransactionUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Optional;

@Repository
public interface TransactionUserRepository extends JpaRepository<TransactionUser, UUID> {

    boolean existsByEmail(String email);

    Optional<TransactionUser> findByEmail(String email);

}
