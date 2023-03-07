package com.pentspace.usermgtservice.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AccountServiceLinkDTO {
    @NotBlank(message = "accountId can not be empty")
    private String accountId;
    @NotBlank(message = "serviceId can not be empty")
    private String serviceId;
}
