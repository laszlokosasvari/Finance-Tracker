-- 1. Create the sequence Hibernate is looking for
-- Hibernate uses 50 as a default 'allocationSize' for sequences
CREATE SEQUENCE IF NOT EXISTS refresh_token_seq START WITH 1 INCREMENT BY 50;

-- 2. Create the User table first (because Refresh Token depends on it)
CREATE TABLE IF NOT EXISTS transaction_user (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 3. Create the Refresh Token table
CREATE TABLE IF NOT EXISTS refresh_token (
    id BIGINT PRIMARY KEY,
    token TEXT UNIQUE NOT NULL,
    expiry_date TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id UUID NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES transaction_user(id) ON DELETE CASCADE
);