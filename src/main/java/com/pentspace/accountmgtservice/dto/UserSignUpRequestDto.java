package com.pentspace.accountmgtservice.dto;

import com.pentspace.accountmgtservice.entities.enums.Roles;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class UserSignUpRequestDto {

    @NotEmpty(message = "firstname can not be empty")
    private String firstName;

    @NotEmpty(message = "lastname can not be empty")
    private String lastName;

    @NotEmpty(message = "phoneNumber can not be empty")
    private String phoneNumber;

    @NotEmpty(message = "country can not be empty")
    private String country;

    @Email(message = "Invalid Mail")
    @NotEmpty(message = "email can not be empty")
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{4,12}$",
            message = "password must be min 4 and max 12 length containing atleast 1 uppercase, 1 lowercase, 1 special character and 1 digit ")
    @NotEmpty(message = "password can not be empty")
    private String password;

}
