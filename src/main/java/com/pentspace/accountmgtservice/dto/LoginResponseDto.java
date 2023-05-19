package com.pentspace.accountmgtservice.dto;

import com.pentspace.accountmgtservice.entities.Address;
import com.pentspace.accountmgtservice.entities.BankDetail;
import com.pentspace.accountmgtservice.entities.Service;
import com.pentspace.accountmgtservice.entities.enums.AccountStatus;
import com.pentspace.accountmgtservice.entities.enums.AccountType;
import com.pentspace.accountmgtservice.entities.enums.Roles;
import com.pentspace.accountmgtservice.security.securityUtils.JWTToken;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Data
public class LoginResponseDto {

    private String id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String password;
    
    private Roles roles;

    private String country;

    private String email;

    private Roles role;
    private JWTToken token;
}
