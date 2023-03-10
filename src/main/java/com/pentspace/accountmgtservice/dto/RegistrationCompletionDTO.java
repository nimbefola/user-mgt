package com.pentspace.accountmgtservice.dto;

import lombok.Data;

@Data
public class RegistrationCompletionDTO {
    private String accountId;
    private String otp;
}
