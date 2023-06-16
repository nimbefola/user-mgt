package com.pentspace.accountmgtservice.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class BankDTO {


    private String cbnBankCode;


    private String bankCode;


    private String bankName;
}
