package com.pentspace.usermgtservice.services;

import com.pentspace.usermgtservice.entities.Bank;

import java.util.List;

public interface BankService{
    List<Bank> getAllBank();
    Bank getBankById(String id);
}
