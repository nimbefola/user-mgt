package com.pentspace.accountmgtservice.dto;

import com.pentspace.accountmgtservice.security.securityUtils.JWTToken;
import lombok.Data;

@Data
public class LoginDTO {
    private String email;
    private String password;

}
