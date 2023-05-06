package com.pentspace.accountmgtservice.dto;

public class ValidateDto {

    private String email;

    private String token;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {

        return email;
    }

    public String getToken() {
        return token;
    }
}
