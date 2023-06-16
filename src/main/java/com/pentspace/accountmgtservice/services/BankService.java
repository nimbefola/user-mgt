package com.pentspace.accountmgtservice.services;

import com.pentspace.accountmgtservice.dto.BankDTO;
import com.pentspace.accountmgtservice.entities.Bank;
import com.pentspace.accountmgtservice.entities.BankDetail;

import java.util.List;

public interface BankService{

    Bank createBank(BankDTO bankDTO);
    List<Bank> getAllBank();
    Bank getBankById(String id);
}
