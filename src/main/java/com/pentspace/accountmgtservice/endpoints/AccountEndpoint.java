package com.pentspace.accountmgtservice.endpoints;

import com.pentspace.accountmgtservice.response.ApiSuccessResponse;
import com.pentspace.accountmgtservice.clients.PaystackServiceClient;
import com.pentspace.accountmgtservice.dto.*;
import com.pentspace.accountmgtservice.entities.Account;
import com.pentspace.accountmgtservice.entities.enums.AccountStatus;
import com.pentspace.accountmgtservice.exceptions.*;
import com.pentspace.accountmgtservice.handlers.AccountHandler;
import com.pentspace.accountmgtservice.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/account")
@CrossOrigin(origins = "*")
public class AccountEndpoint {
    @Autowired
    private AccountHandler accountHandler;

    @Autowired
    private UserServices userServices;
    @Autowired
    private PaystackServiceClient paystackServiceClient;

        @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody @Valid UserSignUpRequestDto userSignUpRequestDto) throws MessagingException, GeneralServiceException, AccountCreationException {

        UserSignUpResponseDto result = userServices.signUp(userSignUpRequestDto);

        return  ApiSuccessResponse.generateResponse("Successfully created user",HttpStatus.OK,result);

    }

    @PostMapping(path = "/validate")
    public ResponseEntity<Object> validate(@RequestBody @Valid ValidateDto request) throws MessagingException, GeneralServiceException, IncorrectPasswordException {

        ValidateResponseDto result = userServices.validateAccount(request);
        return  ApiSuccessResponse.generateResponse("Successfully Validated",HttpStatus.OK,result);

    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO request) throws IncorrectPasswordException, GeneralServiceException {

        LoginResponseDto result = userServices.login(request);
        return  ApiSuccessResponse.generateResponse("Successful Login",HttpStatus.OK,result);
    }

    @PostMapping(path = "/changePassword")
    public ResponseEntity<Object> changePassword(@RequestBody @Valid ChangePasswordDTO request,@RequestParam("authentication") String authentication) throws AuthorizationException, MessagingException, GeneralServiceException {

        ChangePasswordDTO result = userServices.changePassword(request,authentication);
        return  ApiSuccessResponse.generateResponse("Password Successfully changed",HttpStatus.OK,result);
    }

    @PostMapping(path = "/forgotPassword")
    public ResponseEntity<Object> forgotPassword(@RequestBody @Valid ForgotPasswordDTO request) throws MessagingException, GeneralServiceException {

        ForgotPasswordDTO result = userServices.forgotPassword(request);
        return  ApiSuccessResponse.generateResponse("Token sent!Kindly validate and reset password",HttpStatus.OK,result);
    }

    @PostMapping(path = "/validateTokenAndPassword")
    public ResponseEntity<Object> retrievePassword(@RequestBody @Valid RetrieveForgotPasswordDTO request) throws MessagingException, GeneralServiceException {
        RetrieveForgotPasswordDTO result = userServices.retrieveForgottenPassword(request);
        return  ApiSuccessResponse.generateResponse("User account successfully retrieved",HttpStatus.OK,result);

    }

    //,String authentication

    @PostMapping(path = "/create", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> create(@RequestBody @Valid AccountDTO request,@RequestParam String userId) throws MessagingException, GeneralServiceException, AuthorizationException, AccountCreationException {

        return new ResponseEntity<>(accountHandler.createAccount(request,userId),  HttpStatus.OK);

    }


    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {

            return new ResponseEntity<>(accountHandler.getById(id), HttpStatus.OK);
    }


    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getAll(@RequestParam("authentication") String authentication) throws AuthorizationException, AccountCreationException {
        return new ResponseEntity<>(accountHandler.getAccounts(authentication), HttpStatus.OK);

    }

    @PutMapping(path = "/status/update", produces = "application/json")
    public ResponseEntity<?> updateAccountStatus(@RequestParam("id") String id, @RequestParam("status") String status,@RequestParam("authentication") String authentication) throws AuthorizationException, GeneralServiceException, AccountCreationException {

        return new ResponseEntity<>(accountHandler.updateAccountStatus(id, AccountStatus.valueOf(status), authentication), HttpStatus.OK);

    }
    @PostMapping(path = "profile/picture/upload/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadProfilePicture(@PathVariable("id") String id, @RequestParam("file") MultipartFile file,@RequestParam("authentication") String authentication) throws AuthorizationException, AccountCreationException {

        return new ResponseEntity<>(accountHandler.uploadProfilePicture(id, file,authentication),HttpStatus.OK);
    }
    @PostMapping(path = "service/link", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> linkAccountToService(@RequestBody @Valid AccountServiceLinkDTO accountServiceLinkDTO,@RequestParam("authentication") String authentication) throws AuthorizationException, AccountCreationException {

        return new ResponseEntity<>(accountHandler.linkAccountWithService(accountServiceLinkDTO.getAccountId(), accountServiceLinkDTO.getServiceId(),authentication), HttpStatus.OK);
    }
    @GetMapping(path = "enquiry/{msisdn}", produces = "application/json")
    public ResponseEntity<?> getAccountByMsisdn(@PathVariable("msisdn") String msisdn,@RequestParam("authentication") String authentication) throws AuthorizationException, AccountCreationException {

        return new ResponseEntity<>(accountHandler.enquiry(msisdn,authentication), HttpStatus.OK);

    }

    @PostMapping(path = "/transfer", produces = "application/json")
    public ResponseEntity<String> transfer(@RequestBody TransferDTO transferDTO,@RequestParam("authentication") String authentication){

        return new ResponseEntity<>(accountHandler.transfer(transferDTO,authentication), HttpStatus.OK); //,authentication

    }
    @PostMapping(path = "/withdraw", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> withdraw(@RequestBody WithdrawDTO withdrawDTO,@RequestParam("authentication") String authentication){

        return new ResponseEntity<>(accountHandler.withdraw(withdrawDTO,authentication), HttpStatus.OK);

    }
    @PutMapping(path = "/payment/{beneficiaryId}/{externalTransactionId}", produces = "application/json")
    public ResponseEntity<String> payment(@PathVariable("beneficiaryId") String beneficiaryId, @PathVariable("externalTransactionId") String externalTransactionId,@RequestParam("authentication") String authentication){

        return new ResponseEntity<>(accountHandler.payment(beneficiaryId, externalTransactionId,authentication), HttpStatus.OK);

    }

    @GetMapping(path = "/payment/status/check/{externalTransactionId}", produces = "application/json")
    public ResponseEntity<?> getPaymentStatus(@PathVariable("externalTransactionId") String externalTransactionId,@RequestParam("authentication") String authentication){

        return new ResponseEntity<>(paystackServiceClient.verifyPayment(externalTransactionId), HttpStatus.OK);

    }

    @PutMapping(path = "balance/update",consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updateBalances(@RequestBody List<Account> accounts,@RequestParam("authentication") String authentication) throws AuthorizationException, AccountCreationException {

        return new ResponseEntity<>(accountHandler.updateBalances(accounts,authentication), HttpStatus.OK);

    }

}
