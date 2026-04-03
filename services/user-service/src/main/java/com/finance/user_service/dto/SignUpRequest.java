package com.finance.user_service.dto;

import com.finance.user_service.model.TransactionUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpRequest (

        @NotBlank @Email String email,

        @NotBlank String username,

        @NotBlank
        @Size(min = 8, max = 100)
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$",
                message = "Password must contain uppercase, lowercase, and a number")
        String password
) {
    public TransactionUser mapToEntity(String hashed_password){
        return new TransactionUser(null, email, hashed_password, username, 0, false, null);
    }
}