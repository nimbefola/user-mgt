package com.pentspace.usermgtservice.handlers.impl;

import com.pentspace.usermgtservice.clients.EmailServiceClient;
import com.pentspace.usermgtservice.clients.TransactionServiceClient;
import com.pentspace.usermgtservice.dto.AccountDTO;
import com.pentspace.usermgtservice.dto.RegistrationNotificationDTO;
import com.pentspace.usermgtservice.dto.Transaction;
import com.pentspace.usermgtservice.entities.Account;
import com.pentspace.usermgtservice.entities.Address;
import com.pentspace.usermgtservice.entities.Service;
import com.pentspace.usermgtservice.entities.enums.AccountStatus;
import com.pentspace.usermgtservice.entities.enums.TransactionStatus;
import com.pentspace.usermgtservice.entities.enums.TransactionType;
import com.pentspace.usermgtservice.handlers.AccountHandler;
import com.pentspace.usermgtservice.handlers.BaseHandler;
import com.pentspace.usermgtservice.handlers.HashManagerHandler;
import com.pentspace.usermgtservice.services.AccountService;
import com.pentspace.usermgtservice.services.FileUploadService;
import com.pentspace.usermgtservice.services.ServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;

@Component
@Slf4j
public class AccountHandlerImpl extends BaseHandler implements AccountHandler {
    @Autowired
    private AccountService accountService;
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private EmailServiceClient emailServiceClient;
    @Autowired
    private HashManagerHandler hashManagerHandler;
    @Autowired
    private TransactionServiceClient transactionServiceClient;
    @Override
    public Account createAccount(AccountDTO accountDTO) {
        Account account = prepareAccountEntity(accountDTO);
        account =  accountService.create(account);
        if(Objects.nonNull(account.getId())){
            RegistrationNotificationDTO registrationNotificationDTO = new RegistrationNotificationDTO();
            registrationNotificationDTO.setUserEmail(account.getEmail());
            registrationNotificationDTO.setContent(account.getActivationOtp());
            log.info(" Response from email client [{}] ", emailServiceClient.sendEmail(account.getEmail(), account.getActivationOtp(), "REGISTRATION OTP"));
        }
        return account;
    }

    @Override
    public Account getById(String id) {
        Account account =  accountService.getById(id);
        if(Objects.nonNull(account.getProfilePictureUrl())){
            account.setProfileImageBase64(fileUploadService.readAndConvertImageToBase64Read(account.getId()));
        }
        return account;
    }

    @Override
    public Account authenticateAccount(String username, String password) {
        Account account = accountService.getByUsername(username);
        if(!hashManagerHandler.validateData(password, account.getPassword())){
            throw new NoSuchElementException("Invalid Credential, please enter a valid username and password");
        }
        return account;
    }

    @Override
    public Account activate(String id, String OTP) {
        Account account = getById(id);
        if(!account.getActivationOtp().equalsIgnoreCase(OTP)){
            log.error(" Invalid OTP [{}]", OTP);
            throw new RuntimeException("Invalid OTP");
        }
        account.setStatus(AccountStatus.ACTIVE);
        return accountService.updateAccount(id, account);
    }

    @Override
    public Account updateAccountStatus(String id, AccountStatus status) {
        return accountService.updateAccountStatus(id, status);
    }

    @Override
    public List<Account> getAccounts() {
        return accountService.getAccounts();
    }

    @Override
    public Account updateAccount(String id, Account account) {
        return accountService.updateAccount(id, account);
    }

    @Override
    public Account uploadProfilePicture(String id, MultipartFile multipartFile) {
        Account account = accountService.getById(id);
        account.setProfilePictureUrl(fileUploadService.uploadFile(id, multipartFile));
        return accountService.updateAccount(id, account);
    }

    @Override
    public Account linkAccountWithService(String accountId, String  serviceId) {
        Account account = getById(accountId);
        Service service = serviceService.getByID(serviceId);
        account.getServices().add(service);
        return accountService.updateAccount(accountId, account);
    }

    @Override
    public String debitBalance(String id, BigDecimal amount) {
        try {
            Account account = getById(id);
            account.setBalance(account.getBalance().subtract(amount));
            accountService.updateAccount(id, account);
            return "Successful";
        }catch (Exception exception){
            log.error(" An error occurred [{}]", exception.getMessage(), exception);
            return "Failed";
        }
    }

    @Override
    public String creditBalance(String id, BigDecimal amount) {
        try {
            Account account = getById(id);
            account.setBalance(account.getBalance().add(amount));
            accountService.updateAccount(id, account);
            return "Successful";
        }catch (Exception exception){
            log.error(" An error occurred [{}]", exception.getMessage(), exception);
            return "Failed";
        }
    }

    @Override
    public String deposit(String beneficiaryId, String externalTransactionId) {
       try {
       // Todo, we need to call third-party api [Paystack] for now to confirm the transaction status, if approved then we add the amount to beneficiary's current balance
           return null;
       }catch (Exception exception){
           return null;
       }
    }

    @Override
    public String withdraw(String beneficiaryId, String amount) {
        // This is where Pentspace do payout i.e transfer money from personal account to beneficiary real bank account

       try {
          Account account = getById(beneficiaryId);
          if(!isBalanceEnough(account.getBalance(), new BigDecimal(amount))){
              log.info( "Balance is lesser than withdraw amount");
              return "Failed";
          }
          Transaction transaction = prepareWithdrawRequest(amount, beneficiaryId);
          transaction = transactionServiceClient.create(transaction);
          if(Objects.isNull(transaction.getId())){
              return "Failed";
          }
          account.setBalance(account.getBalance().subtract(new BigDecimal(amount)));
          account = accountService.updateAccount(beneficiaryId, account);
          emailServiceClient.sendEmail(account.getEmail(), transaction.getOtp(), "WITHDRAWAL OTP");
           return "Successful";
       }catch (Exception exception){
           return "Failed";
       }
    }

    @Override
    public String transfer(String sourceId, String beneficiaryId, String amount ) {
        //  P2P Fund transfer between pairs on the system (Pentspace)
        try {
            Account sourceAccount = getById(sourceId);
            Account beneficiaryAccount = getById(beneficiaryId);
            if(!isBalanceEnough(sourceAccount.getBalance(), new BigDecimal(amount))){
                return "Balance is lesser than transfer amount";
            }
            sourceAccount.setBalance(sourceAccount.getBalance().subtract(new BigDecimal(amount)));
            beneficiaryAccount.setBalance(beneficiaryAccount.getBalance().add(new BigDecimal(amount)));
            accountService.updateAccounts(Arrays.asList(sourceAccount, beneficiaryAccount));
            Transaction transaction =  prepareTransferTransaction(amount, sourceId, beneficiaryId);
            transaction = transactionServiceClient.create(transaction);
            if (Objects.isNull(transaction.getId())) {
                return "Failed";
            }
            emailServiceClient.sendEmail(sourceAccount.getEmail(), transaction.getOtp(), "TRANSFER OTP");
            return "Successful";
        }catch (Exception exception){
            log.error(" An error occurred [{}]", exception.getMessage(), exception);
            return "Failed";
        }
    }

    private Account prepareAccountEntity(AccountDTO accountDTO){

        Address address = Address.build(
                accountDTO.getAddress().getLine1(),
                accountDTO.getAddress().getLine2(),
                accountDTO.getAddress().getState(),
                accountDTO.getAddress().getCountry()
        );
        String otp = generateOTP();
        Account account = Account.build(
                accountDTO.getName(),
                accountDTO.getBusinessName(),
                accountDTO.getEmail(),
                accountDTO.getUsername(),
                hashManagerHandler.hashData(accountDTO.getPassword()),
                hashManagerHandler.hashData(accountDTO.getPin()),
                null,
                accountDTO.getMsisdn(),
                otp,
                AccountStatus.INACTIVE,
                accountDTO.getAccountType(),
                null,
                null,
                address,
                BigDecimal.ZERO
        );
        return account;
    }

    private boolean isBalanceEnough(BigDecimal balance, BigDecimal amount){
        if(balance.compareTo(amount) >= 0){
            return true;
        }
        return false;
    }

    private Transaction prepareWithdrawRequest(String amount, String beneficiaryId){
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setAmount(new BigDecimal(amount));
        transaction.setDestinationAccount(beneficiaryId);
        transaction.setStatus(TransactionStatus.SCHEDULED);
        transaction.setSourceAccount("Pentspace");
        return transaction;
    }

    private Transaction prepareTransferTransaction(String amount,String sourceId, String beneficiaryId){
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setAmount(new BigDecimal(amount));
        transaction.setDestinationAccount(beneficiaryId);
        transaction.setStatus(TransactionStatus.SCHEDULED);
        transaction.setSourceAccount(sourceId);
        return transaction;
    }

}
