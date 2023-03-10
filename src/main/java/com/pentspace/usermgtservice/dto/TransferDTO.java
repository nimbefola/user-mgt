package com.pentspace.usermgtservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferDTO {
    private String sourceAccountId;
    private String beneficiaryAccountId;
    private String amount;
    private String transactionPin;
}
