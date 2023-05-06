package com.pentspace.accountmgtservice.dto;

import lombok.Data;

@Data
public class RetrieveForgotPasswordDTO {

    private String token;

    private String email;
    private String newPassword;
    private String confirmPassword;

}
