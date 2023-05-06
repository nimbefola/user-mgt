package com.pentspace.accountmgtservice.entities;

import com.pentspace.accountmgtservice.entities.enums.AccountStatus;
import com.pentspace.accountmgtservice.entities.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.math.BigDecimal;
import java.util.Set;


@Entity
@AllArgsConstructor(staticName = "build")
@NoArgsConstructor
public class Account extends Base{
    private String name;
    private String businessName;
    @Email(message = "Please provide a valid Email")
    @Column(unique = true)
    private String email;
    private String username;
    private String password;
    private String pin;
    private String profilePictureUrl;
    private String msisdn;
   // private String activationOtp; //we have here
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    @OneToOne( cascade = CascadeType.ALL)
    private BankDetail bankDetail;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "account_service",
            joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "service_id", referencedColumnName = "id"))
    private Set<Service> services;
    @OneToOne( cascade = CascadeType.ALL)
    private Address address;
    private BigDecimal balance;

    private Boolean locked = false;

    private Boolean enabled = false;

    private String validationToken;
    private String resetPasswordToken;

    public Account(String name, String businessName, String email, String username, String password, String pin, String profilePictureUrl, String msisdn, AccountStatus status, AccountType accountType, BankDetail bankDetail, Set<Service> services, Address address, BigDecimal balance) {
        this.name = name;
        this.businessName = businessName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.pin = pin;
        this.profilePictureUrl = profilePictureUrl;
        this.msisdn = msisdn;
        this.status = status;
        this.accountType = accountType;
        this.bankDetail = bankDetail;
        this.services = services;
        this.address = address;
        this.balance = balance;
    }

    public Account(String name, String businessName, String email, String username, String hashData, String hashData1, String profilePictureUrl, String msisdn, Object o, AccountStatus active, AccountType accountType, BankDetail bankDetail, Address address, Address address1, BigDecimal zero) {
        super();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Set<Service> getServices() {
        return services;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BankDetail getBankDetail() {
        return bankDetail;
    }

    public void setBankDetail(BankDetail bankDetail) {
        this.bankDetail = bankDetail;
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
}
