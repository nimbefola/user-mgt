package com.pentspace.accountmgtservice.handlers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pentspace.accountmgtservice.clients.PaystackServiceClient;
import com.pentspace.accountmgtservice.clients.TransactionServiceClient;
import com.pentspace.accountmgtservice.dto.*;
import com.pentspace.accountmgtservice.emailService.EmailService;
import com.pentspace.accountmgtservice.entities.*;
import com.pentspace.accountmgtservice.entities.enums.AccountStatus;
import com.pentspace.accountmgtservice.entities.enums.TransactionSource;
import com.pentspace.accountmgtservice.entities.enums.TransactionStatus;
import com.pentspace.accountmgtservice.entities.repositories.AccountRepository;
import com.pentspace.accountmgtservice.exceptions.AccountCreationException;
import com.pentspace.accountmgtservice.exceptions.AuthorizationException;
import com.pentspace.accountmgtservice.exceptions.GeneralServiceException;
import com.pentspace.accountmgtservice.exceptions.IncorrectPasswordException;
import com.pentspace.accountmgtservice.handlers.AccountHandler;
import com.pentspace.accountmgtservice.handlers.BaseHandler;
import com.pentspace.accountmgtservice.handlers.HashManagerHandler;
import com.pentspace.accountmgtservice.security.securityServices.UserPrincipalService;
import com.pentspace.accountmgtservice.security.securityUtils.JWTToken;
import com.pentspace.accountmgtservice.services.AccountService;
import com.pentspace.accountmgtservice.services.BankService;
import com.pentspace.accountmgtservice.services.FileUploadService;
import com.pentspace.accountmgtservice.services.ServiceService;
import com.pentspace.accountmgtservice.entities.enums.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.math.BigDecimal;
import java.time.Instant;
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
//    @Autowired
//    private EmailServiceClient emailServiceClient;
    @Autowired
    private HashManagerHandler hashManagerHandler;
    @Autowired
    private TransactionServiceClient transactionServiceClient;

    @Autowired
    private PaystackServiceClient paystackServiceClient;
    @Autowired
    private BankService bankService;

    @Autowired
    private AccountRepository accountRepository;
    @Override
    public Account createAccount(AccountDTO accountDTO,String authentication) throws AuthorizationException, AccountCreationException {


        Account account = prepareAccountEntity(accountDTO);
        account =  accountService.create(account,authentication);


//        if(Objects.nonNull(account.getId())){
//            RegistrationNotificationDTO registrationNotificationDTO = new RegistrationNotificationDTO();
//            registrationNotificationDTO.setUserEmail(account.getEmail());
//            registrationNotificationDTO.setContent(account.getActivationOtp());
//            log.info(" Response from email client [{}] ", emailServiceClient.sendEmail(account.getEmail(), account.getActivationOtp(), "REGISTRATION OTP"));
//        }
        return account;
    }

    @Override
    public Account getById(String id) {
        return accountService.getById(id);
    }

//    @Override
//    public Account authenticateAccount(String username, String password) {
//        Account account = accountService.getByUsername(username);
//        if(!hashManagerHandler.validateData(password, account.getPassword())){
//            throw new NoSuchElementException("Invalid Credential, please enter a valid username and password");
//        }
//        return account;
//    }

    // email service has exceeded will bring this up once settled
//    @Override
//    public Account activate(String id, String OTP) {
//        Account account = getById(id);
//        if(!account.getActivationOtp().equalsIgnoreCase(OTP)){
//            log.error(" Invalid OTP [{}]", OTP);
//            throw new RuntimeException("Invalid OTP");
//        }
//        account.setStatus(AccountStatus.ACTIVE);
//        return accountService.updateAccount(id, account);
//    }

    @Override
    public Account updateAccountStatus(String id, AccountStatus status,String authentication) throws AuthorizationException, AccountCreationException {
        return accountService.updateAccountStatus(id, status,authentication);
    }

    @Override
    public List<Account> getAccounts(String authentication) throws AuthorizationException, AccountCreationException {

        return accountService.getAccounts(authentication);
    }

    @Override
    public Account updateAccount(String id, Account account,String authentication) throws AuthorizationException, AccountCreationException {
        return accountService.updateAccount(id, account,authentication);
    }

    @Override
    public Account uploadProfilePicture(String id, MultipartFile multipartFile,String authentication) throws AuthorizationException, AccountCreationException {
        Account account = accountService.getById(id);
        account.setProfilePictureUrl(fileUploadService.uploadFile(id, multipartFile));
        return accountService.updateAccount(id, account,authentication);
    }

    @Override
    public Account linkAccountWithService(String accountId,String serviceId,String authentication) throws AuthorizationException, AccountCreationException {
        Account account = getById(accountId);
        Service service = serviceService.getByID(serviceId);
        account.getServices().add(service);
        return accountService.updateAccount(accountId, account,authentication);
    }

    @Override
    public String payment(String beneficiaryId, String externalTransactionId,String authentication) {
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
                        accountService.updateAccount(beneficiaryId, account,authentication);
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
    public String withdraw(WithdrawDTO withdrawDTO,String authentication) {
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
            account = accountService.updateAccount(withdrawDTO.getBeneficiaryAccountId(), account,authentication);
            log.info(" Current Balance [{}] ", account.getBalance());
            return "Successful";
        }catch (Exception exception){
            return "Failed";
        }
    }

    @Override
    public String transfer(TransferDTO transferDTO,String authentication) {
        //  P2P Fund transfer between pairs on the system (Pentspace)
        try {
            Account sourceAccount = getById(transferDTO.getSourceAccountId());
            log.info(" Initiating Transfer request from source [{}] to beneficiary [{}] ", transferDTO.getSourceAccountId(), transferDTO.getBeneficiaryAccountId());
            Account beneficiaryAccount = getById(transferDTO.getBeneficiaryAccountId());
            if (!isBalanceEnough(sourceAccount.getBalance(), new BigDecimal(transferDTO.getAmount()))) {
                return "Balance is lesser than transfer amount";
            }
            validatePin(sourceAccount.getPin(), transferDTO.getTransactionPin());
            Transaction transaction = prepareTransferTransaction(transferDTO.getAmount(), transferDTO.getSourceAccountId(), transferDTO.getBeneficiaryAccountId());
            transaction = transactionServiceClient.create(transaction);
            if (Objects.isNull(transaction.getId())) {
                return "Failed";
            }
            log.info(" Initiating Source Balance [{}] ", sourceAccount.getBalance());
            log.info(" Initiating Beneficiary Balance [{}] ", beneficiaryAccount.getBalance());
            sourceAccount.setBalance(sourceAccount.getBalance().subtract(new BigDecimal(transferDTO.getAmount())));
            beneficiaryAccount.setBalance(beneficiaryAccount.getBalance().add(new BigDecimal(transferDTO.getAmount())));
            accountService.updateAccounts(Arrays.asList(sourceAccount, beneficiaryAccount), authentication);
            log.info(" Current Source Balance [{}] ", sourceAccount.getBalance());
            log.info(" Current Beneficiary Balance [{}] ", beneficiaryAccount.getBalance());
            return "Successful";
        } catch (RuntimeException | AccountCreationException exception) {
            log.error(" An error occurred [{}]", exception.getMessage(), exception);
            return "Failed";
        } catch (AuthorizationException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Account enquiry(String msisdn,String authentication) throws AuthorizationException, AccountCreationException {
        return accountService.getByMsisdn(msisdn,authentication);
    }

    @Override
    public List<Account> updateBalances(List<Account> accounts,String authentication) throws AuthorizationException, AccountCreationException {
        List<Account> savedAccounts = new ArrayList<>();
        accounts.forEach(account -> {
            Account savedAccount =  getById(account.getId());
            savedAccount.setBalance(account.getBalance());
            savedAccounts.add(savedAccount);
        });
        return accountService.updateAccounts(savedAccounts,authentication);
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

        //String otp = generateOTP();
        Account account = Account.build(
                accountDTO.getName(),
                accountDTO.getBusinessName(),
                accountDTO.getEmail(),
                accountDTO.getUsername(),
            //    hashManagerHandler.hashData(accountDTO.getPassword()),
                hashManagerHandler.hashData(accountDTO.getPin()),
                null,
                accountDTO.getMsisdn(),
                null,
                AccountStatus.ACTIVE,
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
        transaction.setTransactionSource(TransactionSource.ACCOUNT);
        return transaction;
    }

    private Transaction prepareTransferTransaction(String amount,String sourceId, String beneficiaryId){
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setAmount(new BigDecimal(amount));
        transaction.setDestinationAccount(beneficiaryId);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setSourceAccount(sourceId);
        transaction.setTransactionSource(TransactionSource.ACCOUNT);
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
        transaction.setTransactionSource(TransactionSource.ACCOUNT);
        return transaction;
    }

    public void validatePin(String accountPin, String transactionPin){
        if(!hashManagerHandler.validateData(transactionPin, accountPin)){
            throw new RuntimeException("Invalid Pin");
        }
    }

}
