package com.pentspace.usermgtservice.dto;

import lombok.Data;

@Data
public class RegistrationCompletionDTO {
    private String accountId;
    private String otp;
}
