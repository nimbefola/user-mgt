package com.pentspace.accountmgtservice.dto;

import com.pentspace.accountmgtservice.security.securityUtils.JWTToken;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class ValidateResponseDto {

    @Email(message = "Invalid Mail")
    private String email;

    private String token;

    private JWTToken Jwtoken;
}
