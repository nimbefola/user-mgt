package com.pentspace.accountmgtservice.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class BankDetailsDTO {

    @Size(min = 10, max = 10, message = "Account number must be 10 digits")
    @Pattern(regexp = "^\\d{10}$", message = "Account number must be 10 digits")
    private String accountNumber;
    private String cbnBankCode;
    private String bankCode;
    private String accountName;
    private String bankName;
}
