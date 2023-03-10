package com.pentspace.usermgtservice.services;


import com.pentspace.usermgtservice.entities.Account;
import com.pentspace.usermgtservice.entities.enums.AccountStatus;
import com.pentspace.usermgtservice.entities.enums.TransactionStatus;

import java.util.List;

public interface AccountService {
    Account create(Account account);
//    Account getByUsernameAndPassword(String username, String password);
    Account getByUsername(String username);
    Account activate(String id, String OTP);
    Account getById(String id);
    Account updateAccountStatus(String id, AccountStatus status);
    List<Account> getAccounts();
    Account updateAccount(String id, Account account);
    List<Account> updateAccounts(List<Account> accounts);
    Account getByMsisdn(String msisdn);

}
