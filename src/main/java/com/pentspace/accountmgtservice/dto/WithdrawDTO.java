package com.pentspace.accountmgtservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawDTO {
    private String beneficiaryAccountId;
    private String amount;
    private String transactionPin;
}
