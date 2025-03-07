package com.catalog.movies.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email has to have a valid format")
    private String email;

    @NotBlank(message = "Password is mandatory")
    private String password;


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
}