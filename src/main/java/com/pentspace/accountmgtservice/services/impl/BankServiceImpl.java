package com.pentspace.accountmgtservice.services.impl;

import com.pentspace.accountmgtservice.entities.Bank;
import com.pentspace.accountmgtservice.entities.repositories.BankRepository;
import com.pentspace.accountmgtservice.services.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BankServiceImpl implements BankService {
    @Autowired
    private BankRepository bankRepository;
    @Override
    public List<Bank> getAllBank() {
        return bankRepository.findAll();
    }

    @Override
    public Bank getBankById(String id) {
        return bankRepository.findById(id).orElseThrow(()->new NoSuchElementException("Bank not found"));
    }
}
