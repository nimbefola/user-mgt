package com.pentspace.accountmgtservice.services.impl;

import com.pentspace.accountmgtservice.entities.User;
import com.pentspace.accountmgtservice.entities.enums.AccountStatus;
import com.pentspace.accountmgtservice.entities.repositories.AccountRepository;
import com.pentspace.accountmgtservice.entities.Account;
import com.pentspace.accountmgtservice.entities.repositories.UserRepository;
import com.pentspace.accountmgtservice.exceptions.AccountCreationException;
import com.pentspace.accountmgtservice.exceptions.AuthorizationException;
import com.pentspace.accountmgtservice.security.securityServices.UserPrincipalService;
import com.pentspace.accountmgtservice.serviceUtil.IdGenerator;
import com.pentspace.accountmgtservice.services.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserPrincipalService userPrincipalService;

    @Autowired
    private UserRepository userRepository;


//,String authentication

    @Override
    public Account create(Account account) throws AuthorizationException, AccountCreationException {

//        String userEmail = userPrincipalService.getUserEmailAddressFromToken(authentication);
//
//        Optional<User> user = userRepository.findUserByEmail(userEmail);
//        if (!user.isPresent()) {
//            throw new AccountCreationException("User not registered");
//        }

       // account.setId(IdGenerator.generateId());

        return accountRepository.save(account);
    }

    @Override
    public Account getByUsername(String username,String authentication) throws AuthorizationException, AccountCreationException {

        String userEmail = userPrincipalService.getUserEmailAddressFromToken(authentication);

        Optional<User> user = userRepository.findUserByEmail(userEmail);
        if (!user.isPresent()) {
            throw new AccountCreationException("User not registered");
        }

        return accountRepository.findByUsername(username).orElseThrow(()->new
                NoSuchElementException("Account not found"));
    }

//    @Override
//    public Account activate(String id, String OTP) {
//        Account account = accountRepository.findById(id).orElseThrow(()->new NoSuchElementException("Account not found"));
////        try {
////            if(!account.getActivationOtp().equalsIgnoreCase(OTP)){
////                log.error(" Invalid OTP [{}]", OTP);
////                throw new RuntimeException("Invalid OTP");
////            }
////        } catch (RuntimeException exception) {
////            throw exception;
////        }
//        account.setStatus(AccountStatus.ACTIVE);
//        return accountRepository.save(account);
//    }

    @Override
    public Account getById(String id) {
        return accountRepository.findById(id).orElseThrow(()->new NoSuchElementException("Account not found"));
    }

    @Override
    public Account updateAccountStatus(String id, AccountStatus status,String authentication) throws AuthorizationException, AccountCreationException {

        String userEmail = userPrincipalService.getUserEmailAddressFromToken(authentication);

        Optional<User> user = userRepository.findUserByEmail(userEmail);
        if (!user.isPresent()) {
            throw new AccountCreationException("User not registered");
        }

        Account account = getById(id);
        account.setStatus(status);
        return accountRepository.save(account);
    }

    @Override
    public List<Account> getAccounts(String authentication) throws AuthorizationException, AccountCreationException {

        String userEmail = userPrincipalService.getUserEmailAddressFromToken(authentication);

        Optional<User> user = userRepository.findUserByEmail(userEmail);
        if (!user.isPresent()) {
            throw new AccountCreationException("User not registered");
        }

        return accountRepository.findAll();
    }

    @Override
    public Account updateAccount(String id, Account account,String authentication) throws AuthorizationException, AccountCreationException {

        String userEmail = userPrincipalService.getUserEmailAddressFromToken(authentication);

        Optional<User> user = userRepository.findUserByEmail(userEmail);
        if (!user.isPresent()) {
            throw new AccountCreationException("User not registered");
        }

        Account accountToUpdate = getById(id);
        //TODO determine properties to update
        return accountRepository.save(accountToUpdate);
    }

    @Override
    public List<Account> updateAccounts(List<Account> accounts,String authentication) throws AuthorizationException, AccountCreationException {

        String userEmail = userPrincipalService.getUserEmailAddressFromToken(authentication);

        Optional<User> user = userRepository.findUserByEmail(userEmail);
        if (!user.isPresent()) {
            throw new AccountCreationException("User not registered");
        }

        return accountRepository.saveAll(accounts);
    }

    @Override
    public Account getByMsisdn(String msisdn,String authentication) throws AuthorizationException, AccountCreationException {

        String userEmail = userPrincipalService.getUserEmailAddressFromToken(authentication);

        Optional<User> user = userRepository.findUserByEmail(userEmail);
        if (!user.isPresent()) {
            throw new AccountCreationException("User not registered");
        }

        return accountRepository.findByMsisdn(msisdn).orElseThrow(()->new NoSuchElementException("Account not found"));
    }

}
