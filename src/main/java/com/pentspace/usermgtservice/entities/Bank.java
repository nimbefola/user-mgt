package com.pentspace.usermgtservice.entities;


import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Bank extends Base {

    @Column
    private String cbnBankCode;

    @Column
    private String bankCode;

    @Column
    private String bankName;

    public String getCbnBankCode() {
        return cbnBankCode;
    }

    public void setCbnBankCode(String cbnBankCode) {
        this.cbnBankCode = cbnBankCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
