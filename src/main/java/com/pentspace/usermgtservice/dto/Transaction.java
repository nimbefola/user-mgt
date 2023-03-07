package com.pentspace.usermgtservice.dto;

import com.pentspace.usermgtservice.entities.enums.TransactionStatus;
import com.pentspace.usermgtservice.entities.enums.TransactionType;
import lombok.Data;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@Data
public class Transaction {
    private String id;
    private TransactionStatus status;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private String sourceAccount;
    private String destinationAccount;
    private BigDecimal amount;
    private String otp;
    private String otpStatus;
    private String metaData;

}
