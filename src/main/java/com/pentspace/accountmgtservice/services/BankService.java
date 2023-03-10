package com.pentspace.accountmgtservice.services;

import com.pentspace.accountmgtservice.entities.Bank;

import java.util.List;

public interface BankService{
    List<Bank> getAllBank();
    Bank getBankById(String id);
}
