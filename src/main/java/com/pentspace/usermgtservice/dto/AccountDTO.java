package com.pentspace.usermgtservice.dto;

import com.pentspace.usermgtservice.entities.Address;
import com.pentspace.usermgtservice.entities.Service;
import com.pentspace.usermgtservice.entities.enums.AccountType;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Data
public class AccountDTO {
    @NotBlank
    private String name;
    private String businessName;
    @Email(message = "Please provide a valid Email")
    private String email;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String pin;
    private String profilePictureUrl;
    @Pattern(regexp = "^\\d{11}$", message = "MSISDN should be 11 digits numbers only")
    private String msisdn;
    private AccountType accountType;
    private Service service;
    @NotNull
    private Address address;
    private BigDecimal balance;
}
