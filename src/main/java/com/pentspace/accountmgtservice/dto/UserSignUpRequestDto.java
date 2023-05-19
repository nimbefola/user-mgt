package com.pentspace.accountmgtservice.dto;

import com.pentspace.accountmgtservice.entities.enums.Roles;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class UserSignUpRequestDto {

    private String id;

    private String firstName;

    private String lastName;

    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
            message = "Phone number is not valid")
    private String phoneNumber;

    private String country;

    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{4,12}$",
            message = "password must be min 4 and max 12 length containing atleast 1 uppercase, 1 lowercase, 1 special character and 1 digit ")
    private String password;

    private Roles role;

    private String confirmPassword;
}
