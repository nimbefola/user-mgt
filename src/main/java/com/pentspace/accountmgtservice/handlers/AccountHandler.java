package com.pentspace.accountmgtservice.handlers;

import com.pentspace.accountmgtservice.dto.*;
import com.pentspace.accountmgtservice.entities.Account;
import com.pentspace.accountmgtservice.entities.enums.AccountStatus;
import com.pentspace.accountmgtservice.exceptions.AccountCreationException;
import com.pentspace.accountmgtservice.exceptions.AuthorizationException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AccountHandler {

    Account createAccount(AccountDTO accountDTO,String authentication) throws AuthorizationException, AccountCreationException;
    Account getById(String id);
  //  Account authenticateAccount(String username, String password);
    //    Account activate(String id, String OTP);
    Account updateAccountStatus(String id, AccountStatus status,String authentication) throws AuthorizationException, AccountCreationException;
    List<Account> getAccounts(String authentication) throws AuthorizationException, AccountCreationException;
    Account updateAccount(String id, Account account,String authentication) throws AuthorizationException, AccountCreationException;
    Account uploadProfilePicture(String id, MultipartFile multipartFile,String authentication) throws AuthorizationException, AccountCreationException;
    Account linkAccountWithService(String accountId, String serviceId,String authentication) throws AuthorizationException, AccountCreationException;
    String payment(String beneficiaryId, String externalTransactionId,String authentication);
    String withdraw(WithdrawDTO withdrawDTO,String authentication);
    String transfer(TransferDTO transferDTO,String authentication);
    Account enquiry(String msisdn,String authentication) throws AuthorizationException, AccountCreationException;
    List<Account> updateBalances(List<Account> account,String authentication) throws AuthorizationException, AccountCreationException;


}
