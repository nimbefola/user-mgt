package com.pentspace.accountmgtservice.handlers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pentspace.accountmgtservice.clients.EmailServiceClient;
import com.pentspace.accountmgtservice.clients.PaystackServiceClient;
import com.pentspace.accountmgtservice.clients.TransactionServiceClient;
import com.pentspace.accountmgtservice.dto.*;
import com.pentspace.accountmgtservice.entities.Account;
import com.pentspace.accountmgtservice.entities.Address;
import com.pentspace.accountmgtservice.entities.BankDetail;
import com.pentspace.accountmgtservice.entities.Service;
import com.pentspace.accountmgtservice.entities.enums.AccountStatus;
import com.pentspace.accountmgtservice.entities.enums.TransactionStatus;
import com.pentspace.accountmgtservice.handlers.AccountHandler;
import com.pentspace.accountmgtservice.handlers.BaseHandler;
import com.pentspace.accountmgtservice.handlers.HashManagerHandler;
import com.pentspace.accountmgtservice.services.AccountService;
import com.pentspace.accountmgtservice.services.BankService;
import com.pentspace.accountmgtservice.services.FileUploadService;
import com.pentspace.accountmgtservice.services.ServiceService;
import com.pentspace.usermgtservice.dto.*;
import com.pentspace.usermgtservice.entities.*;
import com.pentspace.accountmgtservice.entities.enums.TransactionType;
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
    @Autowired
    private PaystackServiceClient paystackServiceClient;
    @Autowired
    private BankService bankService;
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
        return accountService.getById(id);
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
            log.info("Debit balance request:: Account Id [{}],  initial balance [{}], amount [{}]", id, account.getBalance(), amount);
            account.setBalance(account.getBalance().subtract(amount));
            Transaction transaction = prepareDebitTransaction(amount, id);
            transactionServiceClient.create(transaction);
            accountService.updateAccount(id, account);
            log.info(" Current balance [{}]", account.getBalance());
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
            log.info("Credit balance request:: Account Id [{}],  initial balance [{}], amount [{}]", id, account.getBalance(), amount);
            account.setBalance(account.getBalance().add(amount));
            Transaction transaction = prepareCreditTransaction(amount, id);
            transactionServiceClient.create(transaction);
            accountService.updateAccount(id, account);
            log.info(" Current balance [{}]", account.getBalance());
            return "Successful";
        }catch (Exception exception){
            log.error(" An error occurred [{}]", exception.getMessage(), exception);
            return "Failed";
        }
    }

    @Override
    public String payment(String beneficiaryId, String externalTransactionId) {
       try {
           // Get transaction status from vendor
           // Save it in transaction table
           // If status is successful from vendor ( Meaning Pentspace Account has been credited ) then credit the beneficiary

           //Lets check if we have already save this transaction
           Transaction savedTransaction = transactionServiceClient.getBySourceId(externalTransactionId);
           if(Objects.nonNull(savedTransaction.getId())){
               log.info(" Transaction [{}] already exist with status [{}] ", externalTransactionId, savedTransaction.getStatus());
               return String.format("Transaction %s already exist with status %s", externalTransactionId, savedTransaction.getStatus());
           }
           log.info(" Fetching payment status from vendor ... ");
           PaystackPaymentStatusDTO paystackPaymentStatusDTO = paystackServiceClient.verifyPayment(externalTransactionId);
           if(paystackPaymentStatusDTO.getStatus().equalsIgnoreCase("true") && Objects.nonNull(paystackPaymentStatusDTO.getData())){
               String paystackResponse = new ObjectMapper().writeValueAsString(paystackPaymentStatusDTO);
               Transaction transaction = preparePaymentTransaction(paystackPaymentStatusDTO.getData().get("amount"), beneficiaryId, paystackResponse, externalTransactionId);
               transaction = transactionServiceClient.create(transaction);
               if(Objects.nonNull(transaction.getId())){
                   if(paystackPaymentStatusDTO.getData().get("status").equalsIgnoreCase("success")){
                       Account account = getById(beneficiaryId);
                       account.setBalance(account.getBalance().add(new BigDecimal(paystackPaymentStatusDTO.getData().get("amount"))));
                       accountService.updateAccount(beneficiaryId, account);
                       return "Successful";
                   }else{
                       return paystackPaymentStatusDTO.getData().get("status");
                   }
               }else{
                   return "An error occurred";
               }
           }else{
               log.info(" Payment status response from vendor [{}] ", paystackPaymentStatusDTO);
               return "Payment Status Unknown please contact service provider";
           }
       }catch (Exception exception){
          log.error(" An error occurred [{}]", exception.getMessage(), exception);
          return " An error occurred ";
       }
    }

    @Override
    public String withdraw(WithdrawDTO withdrawDTO) {
        // This is where Pentspace do payout i.e transfer money from personal account to beneficiary real bank account

       try {
          Account account = getById(withdrawDTO.getBeneficiaryAccountId());
          log.info(" Initiating Withdraw request for beneficiary [{}] ", withdrawDTO.getBeneficiaryAccountId());
          if(!isBalanceEnough(account.getBalance(), new BigDecimal(withdrawDTO.getAmount()))){
              log.info( "Balance is lesser than withdraw amount");
              return "Failed";
          }
          validatePin(account.getPin(), withdrawDTO.getTransactionPin());
          Transaction transaction = prepareWithdrawRequest(withdrawDTO.getAmount(), withdrawDTO.getBeneficiaryAccountId());
          transaction = transactionServiceClient.create(transaction);
          if(Objects.isNull(transaction.getId())){
              return "Failed";
          }
          log.info(" Initiating Balance [{}] ", account.getBalance());
          account.setBalance(account.getBalance().subtract(new BigDecimal(withdrawDTO.getAmount())));
          account = accountService.updateAccount(withdrawDTO.getBeneficiaryAccountId(), account);
          log.info(" Current Balance [{}] ", account.getBalance());
          emailServiceClient.sendEmail(account.getEmail(), transaction.getOtp(), " WITHDRAWAL OTP ");
          return "Successful";
       }catch (Exception exception){
           return "Failed";
       }
    }

    @Override
    public String transfer(TransferDTO transferDTO) {
        //  P2P Fund transfer between pairs on the system (Pentspace)
        try {
            Account sourceAccount = getById(transferDTO.getSourceAccountId());
            log.info(" Initiating Transfer request from source [{}] to beneficiary [{}] ",transferDTO.getSourceAccountId(), transferDTO.getBeneficiaryAccountId());
            Account beneficiaryAccount = getById(transferDTO.getBeneficiaryAccountId());
            if(!isBalanceEnough(sourceAccount.getBalance(), new BigDecimal(transferDTO.getAmount()))){
                return "Balance is lesser than transfer amount";
            }
            validatePin(sourceAccount.getPin(), transferDTO.getTransactionPin());
            Transaction transaction =  prepareTransferTransaction(transferDTO.getAmount(), transferDTO.getSourceAccountId(), transferDTO.getBeneficiaryAccountId());
            transaction = transactionServiceClient.create(transaction);
            if (Objects.isNull(transaction.getId())) {
                return "Failed";
            }
            log.info(" Initiating Source Balance [{}] ", sourceAccount.getBalance());
            log.info(" Initiating Beneficiary Balance [{}] ", beneficiaryAccount.getBalance());
            sourceAccount.setBalance(sourceAccount.getBalance().subtract(new BigDecimal(transferDTO.getAmount())));
            beneficiaryAccount.setBalance(beneficiaryAccount.getBalance().add(new BigDecimal(transferDTO.getAmount())));
            accountService.updateAccounts(Arrays.asList(sourceAccount, beneficiaryAccount));
            log.info(" Current Source Balance [{}] ", sourceAccount.getBalance());
            log.info(" Current Beneficiary Balance [{}] ", beneficiaryAccount.getBalance());
            emailServiceClient.sendEmail(sourceAccount.getEmail(), transaction.getOtp(), " TRANSFER OTP ");
            return "Successful";
        }catch (RuntimeException exception){
            log.error(" An error occurred [{}]", exception.getMessage(), exception);
            return "Failed";
        }
    }

    @Override
    public Account enquiry(String msisdn) {
        return accountService.getByMsisdn(msisdn);
    }

    private Account prepareAccountEntity(AccountDTO accountDTO){
        BankDetail bankDetail = null;
        if(Objects.nonNull(accountDTO.getBankDetail())){
            bankDetail = BankDetail.build(
                    accountDTO.getBankDetail().getAccountNumber(),
                    accountDTO.getBankDetail().getCbnBankCode(),
                    accountDTO.getBankDetail().getBankCode(),
                    accountDTO.getBankDetail().getAccountNumber(),
                    accountDTO.getBankDetail().getBankName()
            );
        }

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
                bankDetail,
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

    private Transaction preparePaymentTransaction(String amount, String beneficiaryId, String metaData, String externalTransactionId){
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.PAYMENT);
        transaction.setAmount(new BigDecimal(amount));
        transaction.setDestinationAccount(beneficiaryId);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setSourceAccount(externalTransactionId);
        transaction.setMetaData(metaData);
        return transaction;
    }

    private Transaction prepareDebitTransaction(BigDecimal amount, String beneficiaryId){
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.DEBIT);
        transaction.setAmount(amount);
        transaction.setDestinationAccount(beneficiaryId);
        transaction.setStatus(TransactionStatus.COMPLETED);
        return transaction;
    }

    private Transaction prepareCreditTransaction(BigDecimal amount, String beneficiaryId){
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.CREDIT);
        transaction.setAmount(amount);
        transaction.setDestinationAccount(beneficiaryId);
        transaction.setStatus(TransactionStatus.COMPLETED);
        return transaction;
    }

    public void validatePin(String accountPin, String transactionPin){
        if(!hashManagerHandler.validateData(transactionPin, accountPin)){
            throw new RuntimeException("Invalid Pin");
        }
    }

}
