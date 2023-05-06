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
import com.pentspace.accountmgtservice.exceptions.AuthorizationException;
import com.pentspace.accountmgtservice.exceptions.GeneralServiceException;
import com.pentspace.accountmgtservice.exceptions.IncorrectPasswordException;
import com.pentspace.accountmgtservice.handlers.AccountHandler;
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
public class AccountHandlerImpl  implements AccountHandler { //extends BaseHandler
    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private ServiceService serviceService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmailService emailService;
    @Autowired
    private HashManagerHandler hashManagerHandler;
    @Autowired
    private TransactionServiceClient transactionServiceClient;
    @Autowired
    private UserPrincipalService userPrincipalService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private PaystackServiceClient paystackServiceClient;
    @Autowired
    private BankService bankService;
    @Override
    public Account createAccount(AccountDTO accountDTO) throws MessagingException, GeneralServiceException {
        //Account account = new Account();


        Optional<Account> optionalAccount = accountRepository.findByEmail(accountDTO.getEmail());

        if (optionalAccount.isPresent()) {
            throw new GeneralServiceException("Account already exists");
        }

        Address address = new Address();
        address.setLine1(accountDTO.getAddress().getLine1());
        address.setLine2(accountDTO.getAddress().getLine2());
        address.setCreated(Date.from(Instant.now()));
        address.setUpdated(Date.from(Instant.now()));
        address.setState(accountDTO.getAddress().getState());
        address.setCountry(accountDTO.getAddress().getCountry());

        BankDetail bankDetail = new BankDetail();
        bankDetail.setBankCode(accountDTO.getBankDetail().getBankCode());
        bankDetail.setAccountName(accountDTO.getBankDetail().getAccountName());
        bankDetail.setBankName(accountDTO.getBankDetail().getBankName());
        bankDetail.setCreated(Date.from(Instant.now()));
        bankDetail.setUpdated(Date.from(Instant.now()));
        bankDetail.setCbnBankCode(accountDTO.getBankDetail().getCbnBankCode());

        Account account = new Account();
        account.setName(accountDTO.getName());
        account.setUsername(accountDTO.getUsername());
        account.setBusinessName(accountDTO.getBusinessName());
        account.setEmail(accountDTO.getEmail());
        account.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
        account.setPin(passwordEncoder.encode(accountDTO.getPin()));
        account.setProfilePictureUrl(account.getProfilePictureUrl());
        account.setMsisdn(accountDTO.getMsisdn());
        account.setAccountType(accountDTO.getAccountType());
        account.setStatus(AccountStatus.ACTIVE);
        account.setAddress(address);
        account.setBankDetail(bankDetail);
        account.setBalance(accountDTO.getBalance());


            String confirm = userPrincipalService.sendRegistrationToken(account);

            account.setValidationToken(confirm);

            emailService.sendRegistrationSuccessfulEmail(account, confirm);

         accountRepository.save(account);

        log.info(account.toString());
        return account;
    }

    @Override
    public boolean validateAccount(ValidateDto validateDto) throws GeneralServiceException, MessagingException {

     Optional<Account> optionalAccount = accountRepository.findByEmail(validateDto.getEmail());

     if (!optionalAccount.isPresent()){
         throw new GeneralServiceException("Email cannot be empty");
     }

       Account account = optionalAccount.get();
      if (account.getValidationToken().equals(validateDto.getToken())){

          account.setEnabled(true);
          account.setValidationToken(null);

          log.info(account.getPassword());
          accountRepository.save(account);
          emailService.sendVerificationMessage(account);
          return true;
      } else {
          throw new GeneralServiceException("Invalid validation token");
      }

    }

    @Override
    public LoginResponseDto login(LoginDTO loginDTO) throws IncorrectPasswordException, GeneralServiceException {
         LoginResponseDto loginResponseDto = new LoginResponseDto();
        JWTToken jwtToken = userPrincipalService.loginUser(loginDTO);
        if (jwtToken != null) {
            Optional<Account> users = accountRepository.findByEmail(loginDTO.getEmail());
            if (users.isPresent()) {
                Account account = users.get();
                if (!account.getEnabled()) {
                    throw new GeneralServiceException("User account has not been activated");
                }
            }
            users.ifPresent(entity -> modelMapper.map(entity, loginResponseDto));
            loginResponseDto.setToken(jwtToken);
            log.info(jwtToken.toString());
        }
        log.info(loginResponseDto.toString());
        return loginResponseDto;
    }

    @Override
    public boolean changePassword(ChangePasswordDTO changePasswordDTO, String authentication) throws AuthorizationException, GeneralServiceException, MessagingException {
        String userEmail = userPrincipalService.getUserEmailAddressFromToken(authentication);

       if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())){
           throw new GeneralServiceException("New password do not match");
       }

        Optional<Account> user = accountRepository.findByEmail(userEmail);
        if (!user.isPresent()) {
            throw new GeneralServiceException("User not found");
        }

        boolean matches = userPrincipalService.passwordMatches(
                changePasswordDTO.getOldPassword(), user.get().getPassword());


        if (!matches){
          throw new GeneralServiceException("Old password is incorrect");
        }else
            user.get().setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));

        accountRepository.save(user.get());

        emailService.sendChangePasswordMessage(user.get());

        return true;
    }

    @Override
    public boolean forgotPassword(ForgotPasswordDTO forgotPasswordDTO) throws GeneralServiceException, MessagingException {

        Optional<Account> user = accountRepository.findByEmail(forgotPasswordDTO.getEmail());
        if (!user.isPresent()) {
            throw new GeneralServiceException("No such user exists!");
        }

        String confirmation = userPrincipalService.sendRegistrationToken(user.get());

        user.get().setValidationToken(confirmation);

        accountRepository.save(user.get());

        emailService.sendForgotPasswordMessage(user.get(), confirmation);
        return true;
    }

    @Override
    public boolean retrieveForgottenPassword(RetrieveForgotPasswordDTO retrieveForgotPasswordDTO) throws MessagingException, GeneralServiceException {

        if (!retrieveForgotPasswordDTO.getNewPassword().equals(retrieveForgotPasswordDTO.getConfirmPassword())) {
            throw new GeneralServiceException("New Passwords do not match");
        }
        Optional<Account> users = accountRepository.findByEmail(retrieveForgotPasswordDTO.getEmail());
        if (users.isPresent()) {
            if (users.get().getValidationToken().equals(retrieveForgotPasswordDTO.getToken())) {
                users.get().setEnabled(true);
                users.get().setValidationToken(null);

                users.get().setPassword(passwordEncoder.encode(retrieveForgotPasswordDTO.getNewPassword()));

                accountRepository.save(users.get());

                emailService.resetPasswordConfirmation(users.get());
                return true;
            } else {
                throw new GeneralServiceException("Invalid validation token");
            }
        }
        throw new GeneralServiceException("User With this email does not exist");
    }


    @Override
    public Account getById(String id) {
        return accountService.getById(id);
    }


    @Override
    public Account updateAccountStatus(String id, AccountStatus status,String authentication) throws AuthorizationException, GeneralServiceException {
        String userEmail = userPrincipalService.getUserEmailAddressFromToken(authentication);

        Optional<Account> user = accountRepository.findByEmail(userEmail);
        if (!user.isPresent()) {
            throw new GeneralServiceException("User not found");
        }

        return accountService.updateAccountStatus(id, status);
    }

    @Override
    public List<Account> getAccounts(String authentication) throws AuthorizationException, GeneralServiceException {

        String userEmail = userPrincipalService.getUserEmailAddressFromToken(authentication);

        Optional<Account> user = accountRepository.findByEmail(userEmail);
        if (!user.isPresent()) {
            throw new GeneralServiceException("User not found");
        }

        return accountService.getAccounts();
    }

    @Override
    public Account updateAccount(String id, Account account,String authentication) throws AuthorizationException, GeneralServiceException {

        String userEmail = userPrincipalService.getUserEmailAddressFromToken(authentication);

        Optional<Account> user = accountRepository.findByEmail(userEmail);
        if (!user.isPresent()) {
            throw new GeneralServiceException("User not found");
        }

        return accountService.updateAccount(id, account);
    }

    @Override
    public Account uploadProfilePicture(String id, MultipartFile multipartFile, String authentication) throws AuthorizationException, GeneralServiceException {

        String userEmail = userPrincipalService.getUserEmailAddressFromToken(authentication);

        Optional<Account> user = accountRepository.findByEmail(userEmail);
        if (!user.isPresent()) {
            throw new GeneralServiceException("User not found");
        }

        Account account = accountService.getById(id);
        account.setProfilePictureUrl(fileUploadService.uploadFile(id, multipartFile));
        return accountService.updateAccount(id, account);
    }

    @Override
    public Account linkAccountWithService(String accountId, String  serviceId,String authentication) throws AuthorizationException, GeneralServiceException {

        String userEmail = userPrincipalService.getUserEmailAddressFromToken(authentication);

        Optional<Account> user = accountRepository.findByEmail(userEmail);
        if (!user.isPresent()) {
            throw new GeneralServiceException("User not found");
        }

        Account account = getById(accountId);
        Service service = serviceService.getByID(serviceId);
        account.getServices().add(service);
        return accountService.updateAccount(accountId, account);
    }

    @Override
    public String payment(String beneficiaryId, String externalTransactionId,String authentication) throws GeneralServiceException, AuthorizationException {

        String userEmail = userPrincipalService.getUserEmailAddressFromToken(authentication);

        Optional<Account> user = accountRepository.findByEmail(userEmail);
        if (!user.isPresent()) {
            throw new GeneralServiceException("User not found");
        }

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
    public String withdraw(WithdrawDTO withdrawDTO,String authentication) throws AuthorizationException, GeneralServiceException {
        // This is where Pentspace do payout i.e transfer money from personal account to beneficiary real bank account
        String userEmail = userPrincipalService.getUserEmailAddressFromToken(authentication);

        Optional<Account> user = accountRepository.findByEmail(userEmail);
        if (!user.isPresent()) {
            throw new GeneralServiceException("User not found");
        }

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
          return "Successful";
       }catch (Exception exception){
           return "Failed";
       }
    }


    @Override
    public String transfer(TransferDTO transferDTO,String authentication) throws AuthorizationException, GeneralServiceException {
        //  P2P Fund transfer between pairs on the system (Pentspace)

        String userEmail = userPrincipalService.getUserEmailAddressFromToken(authentication);

        Optional<Account> user = accountRepository.findByEmail(userEmail);
        if (!user.isPresent()) {
            throw new GeneralServiceException("User not found");
        }

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
            return "Successful";
        }catch (RuntimeException exception){
            log.error(" An error occurred [{}]", exception.getMessage(), exception);
            return "Failed";
        }
    }

    @Override
    public Account enquiry(String msisdn,String authentication) throws AuthorizationException, GeneralServiceException {

        String userEmail = userPrincipalService.getUserEmailAddressFromToken(authentication);

        Optional<Account> user = accountRepository.findByEmail(userEmail);
        if (!user.isPresent()) {
            throw new GeneralServiceException("User not found");
        }

        return accountService.getByMsisdn(msisdn);
    }

    @Override
    public List<Account> updateBalances(List<Account> accounts,String authentication) throws AuthorizationException, GeneralServiceException {

        String userEmail = userPrincipalService.getUserEmailAddressFromToken(authentication);

        Optional<Account> user = accountRepository.findByEmail(userEmail);
        if (!user.isPresent()) {
            throw new GeneralServiceException("User not found");
        }

        List<Account> savedAccounts = new ArrayList<>();
        accounts.forEach(account -> {
            Account savedAccount = null;
            savedAccount = getById(account.getId());
//            } catch (AuthorizationException e) {
//                throw new RuntimeException(e);
            savedAccount.setBalance(account.getBalance());
            savedAccounts.add(savedAccount);
        });
        return accountService.updateAccounts(savedAccounts);
    }

//    private Account prepareAccountEntity(AccountDTO accountDTO){  //NOTE WE USE THIS TO PREPARE THE STATEMENT OF ACC
//        BankDetail bankDetail = null;
//        if(Objects.nonNull(accountDTO.getBankDetail())){
//            bankDetail = BankDetail.build(
//                    accountDTO.getBankDetail().getAccountNumber(),
//                    accountDTO.getBankDetail().getCbnBankCode(),
//                    accountDTO.getBankDetail().getBankCode(),
//                    accountDTO.getBankDetail().getAccountNumber(),
//                    accountDTO.getBankDetail().getBankName()
//            );
//        }
//
//        Address address = Address.build(
//                accountDTO.getAddress().getLine1(),
//                accountDTO.getAddress().getLine2(),
//                accountDTO.getAddress().getState(),
//                accountDTO.getAddress().getCountry()
//        );
//
//        //String otp = generateOTP();
//        Account account = new Account(
//                accountDTO.getName(),
//                accountDTO.getBusinessName(),
//                accountDTO.getEmail(),
//                accountDTO.getUsername(),
//                hashManagerHandler.hashData(accountDTO.getPassword()),
//                hashManagerHandler.hashData(accountDTO.getPin()),
//                accountDTO.getProfilePictureUrl(),//null,
//                accountDTO.getMsisdn(),
//                null,
//                AccountStatus.ACTIVE,
//                accountDTO.getAccountType(),
//                bankDetail,
//                accountDTO.getAddress(), //null
//                address,
//                BigDecimal.ZERO
//        );
//        return account;
//    }

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
