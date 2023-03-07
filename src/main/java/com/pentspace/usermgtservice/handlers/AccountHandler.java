package com.pentspace.usermgtservice.handlers;

import com.pentspace.usermgtservice.dto.AccountDTO;
import com.pentspace.usermgtservice.dto.RegistrationCompletionDTO;
import com.pentspace.usermgtservice.entities.Account;
import com.pentspace.usermgtservice.entities.enums.AccountStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface AccountHandler {
    Account createAccount(AccountDTO accountDTO);
    Account getById(String id);
    Account authenticateAccount(String username, String password);
    Account activate(String id, String OTP);
    Account updateAccountStatus(String id, AccountStatus status);
    List<Account> getAccounts();
    Account updateAccount(String id, Account account);
    Account uploadProfilePicture(String id, MultipartFile multipartFile);
    Account linkAccountWithService(String accountId, String serviceId);
    String debitBalance(String id, BigDecimal amount);
    String creditBalance(String id, BigDecimal amount);
    String deposit(String beneficiaryId, String externalTransactionId);
    String withdraw(String beneficiaryId, String amount);
    String transfer(String sourceId, String beneficiaryId, String amount);
}
