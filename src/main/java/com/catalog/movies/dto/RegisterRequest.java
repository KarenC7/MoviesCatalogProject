package com.catalog.movies.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class RegisterRequest {

    @NotBlank(message = "Email mandatory")
    @Email(message = "Email must have a valid format")
    private String email;

    @NotBlank(message = "Mandatory password")
    private String password;


    private String role; // "ADMIN" or "USER"

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}