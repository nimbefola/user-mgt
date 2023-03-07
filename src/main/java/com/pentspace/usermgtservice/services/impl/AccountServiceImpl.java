package com.pentspace.usermgtservice.services.impl;

import com.pentspace.usermgtservice.entities.Account;
import com.pentspace.usermgtservice.entities.enums.AccountStatus;
import com.pentspace.usermgtservice.entities.repositories.AccountRepository;
import com.pentspace.usermgtservice.services.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Override
    public Account create(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account getByUsernameAndPassword(String username, String password) {
        return accountRepository.findByUsernameAndPassword(username, password).orElseThrow(()->new NoSuchElementException("Invalid Credential, please enter a valid username and password"));
    }

    @Override
    public Account getByUsername(String username) {
        return accountRepository.findByUsername(username).orElseThrow(()->new NoSuchElementException("Account not found"));
    }

    @Override
    public Account activate(String id, String OTP) {
        Account account = accountRepository.findById(id).orElseThrow(()->new NoSuchElementException("Account not found"));
            try {
                if(!account.getActivationOtp().equalsIgnoreCase(OTP)){
                    log.error(" Invalid OTP [{}]", OTP);
                    throw new RuntimeException("Invalid OTP");
                }
            } catch (RuntimeException exception) {
                throw exception;
            }
        account.setStatus(AccountStatus.ACTIVE);
        return accountRepository.save(account);
    }

    @Override
    public Account getById(String id) {
        return accountRepository.findById(id).orElseThrow(()->new NoSuchElementException("Account not found"));
    }

    @Override
    public Account updateAccountStatus(String id, AccountStatus status) {
        Account account = getById(id);
        account.setStatus(status);
        return accountRepository.save(account);
    }

    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account updateAccount(String id, Account account) {
        Account accountToUpdate = getById(id);
        //TODO determine properties to update
        return accountRepository.save(accountToUpdate);
    }

    @Override
    public List<Account> updateAccounts(List<Account> accounts) {
        return accountRepository.saveAll(accounts);
    }

}
