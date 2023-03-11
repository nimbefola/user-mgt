package com.pentspace.accountmgtservice.handlers;

import com.pentspace.accountmgtservice.dto.AccountDTO;
import com.pentspace.accountmgtservice.dto.TransferDTO;
import com.pentspace.accountmgtservice.dto.WithdrawDTO;
import com.pentspace.accountmgtservice.entities.Account;
import com.pentspace.accountmgtservice.entities.enums.AccountStatus;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface AccountHandler {
    Account createAccount(AccountDTO accountDTO);
    Account getById(String id);
    Account authenticateAccount(String username, String password);
//    Account activate(String id, String OTP);
    Account updateAccountStatus(String id, AccountStatus status);
    List<Account> getAccounts();
    Account updateAccount(String id, Account account);
    Account uploadProfilePicture(String id, MultipartFile multipartFile);
    Account linkAccountWithService(String accountId, String serviceId);
    String payment(String beneficiaryId, String externalTransactionId);
    String withdraw(WithdrawDTO withdrawDTO);
    String transfer(TransferDTO transferDTO);
    Account enquiry(String msisdn);
    List<Account> updateBalances(List<Account> account);
}
