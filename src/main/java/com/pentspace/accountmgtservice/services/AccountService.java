package com.pentspace.accountmgtservice.services;


import com.pentspace.accountmgtservice.entities.enums.AccountStatus;
import com.pentspace.accountmgtservice.entities.Account;
import com.pentspace.accountmgtservice.exceptions.AccountCreationException;
import com.pentspace.accountmgtservice.exceptions.AuthorizationException;

import java.util.List;

public interface AccountService {

    Account create(Account account,String authentication) throws AuthorizationException, AccountCreationException;
    //    Account getByUsernameAndPassword(String username, String password);
    Account getByUsername(String username,String authentication) throws AuthorizationException, AccountCreationException;
   // Account activate(String id, String OTP);
    Account getById(String id);
    Account updateAccountStatus(String id, AccountStatus status,String authentication) throws AuthorizationException, AccountCreationException;
    List<Account> getAccounts(String authentication) throws AuthorizationException, AccountCreationException;
    Account updateAccount(String id, Account account,String authentication) throws AuthorizationException, AccountCreationException;
    List<Account> updateAccounts(List<Account> accounts,String authentication) throws AuthorizationException, AccountCreationException;
    Account getByMsisdn(String msisdn,String authentication) throws AuthorizationException, AccountCreationException;

}
