package com.pentspace.accountmgtservice.entities;


import com.pentspace.accountmgtservice.entities.enums.AccountType;
import com.pentspace.accountmgtservice.entities.enums.Roles;

import javax.persistence.*;
import java.util.Set;

//A LOT NEEDED TO BE DONE BUT WE FOCUS ON THE SECURITY SURFACE FOR NOW

@Entity
@Table(name = "users")
public class User  {

    @Id
    private String id;

    @Column
    private String firstName;
    @Column
    private String lastName;
    private String email;
    @Column
    private String password;
    @Column
    private String phoneNumber;
    @Column
    private String country;
    @Enumerated
    private Roles roles;

    @Enumerated
    private AccountType accountType;

    @Column
    private Boolean locked = false;
    @Column
    private Boolean enabled = false;
    @Column
    private String validationToken;
    @Column
    private String resetPasswordToken;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public AccountType getAccountType() {

        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }


    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getValidationToken() {
        return validationToken;
    }

    public void setValidationToken(String validationToken) {
        this.validationToken = validationToken;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }
}
