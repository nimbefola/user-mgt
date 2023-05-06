package com.pentspace.accountmgtservice.handlers;

import com.pentspace.accountmgtservice.dto.*;
import com.pentspace.accountmgtservice.entities.Account;
import com.pentspace.accountmgtservice.entities.enums.AccountStatus;
import com.pentspace.accountmgtservice.exceptions.AuthorizationException;
import com.pentspace.accountmgtservice.exceptions.GeneralServiceException;
import com.pentspace.accountmgtservice.exceptions.IncorrectPasswordException;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.math.BigDecimal;
import java.util.List;

public interface AccountHandler {
    Account createAccount(AccountDTO accountDTO) throws MessagingException, GeneralServiceException;

    boolean validateAccount(ValidateDto validateDto) throws GeneralServiceException, MessagingException;
//    Account getById(String id);
    LoginResponseDto login(LoginDTO loginDTO) throws IncorrectPasswordException, GeneralServiceException;

    boolean changePassword(ChangePasswordDTO changePasswordDTO,String authentication) throws AuthorizationException, GeneralServiceException, MessagingException;

    boolean forgotPassword(ForgotPasswordDTO forgotPasswordDTO) throws GeneralServiceException, MessagingException;

    boolean retrieveForgottenPassword(RetrieveForgotPasswordDTO retrieveForgotPasswordDTO) throws MessagingException, GeneralServiceException;

    Account getById(String id);

    Account updateAccountStatus(String id, AccountStatus status, String authentication) throws AuthorizationException, GeneralServiceException;
    List<Account> getAccounts(String authentication) throws AuthorizationException, GeneralServiceException;
    Account updateAccount(String id, Account account,String authentication) throws AuthorizationException, GeneralServiceException;
    Account uploadProfilePicture(String id, MultipartFile multipartFile,String authentication) throws AuthorizationException, GeneralServiceException;

    Account linkAccountWithService(String accountId, String serviceId,String authentication) throws AuthorizationException, GeneralServiceException;

   String payment(String beneficiaryId, String externalTransactionId,String authentication) throws GeneralServiceException, AuthorizationException;
   String withdraw(WithdrawDTO withdrawDTO, String authentication) throws AuthorizationException, GeneralServiceException;
    String transfer(TransferDTO transferDTO,String authentication) throws AuthorizationException, GeneralServiceException;
    Account enquiry(String msisdn,String authentication) throws AuthorizationException, GeneralServiceException;

    List<Account> updateBalances(List<Account> account,String authentication) throws AuthorizationException, GeneralServiceException;
}
