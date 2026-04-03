package com.finance.user_service.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "transaction_user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password_hash")
    private String password_hash;

    @Column(name= "username")
    private String username;

    @Column(name= "failed_attempts")
    private int failed_attempts;

    @Column(name= "account_locked")
    private boolean account_locked;

    @Column(name= "lock_time")
    private Timestamp lock_time;

}
