package com.pentspace.accountmgtservice.endpoints;

import com.pentspace.accountmgtservice.clients.PaystackServiceClient;
import com.pentspace.accountmgtservice.dto.*;
import com.pentspace.accountmgtservice.entities.Account;
import com.pentspace.accountmgtservice.entities.enums.AccountStatus;
import com.pentspace.accountmgtservice.exceptions.AuthorizationException;
import com.pentspace.accountmgtservice.exceptions.GeneralServiceException;
import com.pentspace.accountmgtservice.handlers.AccountHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(path = "/account")
@CrossOrigin(origins = "*")
public class AccountEndpoint {
    @Autowired
    private AccountHandler accountHandler;
    @Autowired
    private PaystackServiceClient paystackServiceClient;

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> create(@RequestBody @Valid AccountDTO request) throws MessagingException, GeneralServiceException {
        try{
        return new ResponseEntity<>(accountHandler.createAccount(request),  HttpStatus.OK);
        }
        catch (Exception exception){
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/validate")
    public ResponseEntity<?> validate(@RequestBody @Valid ValidateDto request) {
        try{
            return new ResponseEntity<>(accountHandler.validateAccount(request), HttpStatus.OK);
        }
        catch (Exception exception){
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO request) throws MessagingException, GeneralServiceException {
        try{
            return new ResponseEntity<>(accountHandler.login(request), HttpStatus.OK);
        }
        catch (Exception exception){
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordDTO request,@RequestParam("authentication") String authentication) throws MessagingException, GeneralServiceException {
        try{
            return new ResponseEntity<>(accountHandler.changePassword(request,authentication), HttpStatus.OK);
        }
        catch (Exception exception){
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid ForgotPasswordDTO request) throws MessagingException, GeneralServiceException {
        try{
            return new ResponseEntity<>(accountHandler.forgotPassword(request), HttpStatus.OK);
        }
        catch (Exception exception){
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/validateTokenAndPassword")
    public ResponseEntity<?> retrievePassword(@RequestBody @Valid RetrieveForgotPasswordDTO request) throws MessagingException, GeneralServiceException {
        try{
            return new ResponseEntity<>(accountHandler.retrieveForgottenPassword(request), HttpStatus.OK);
        }
        catch (Exception exception){
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        try {
            return new ResponseEntity<>(accountHandler.getById(id), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getAll(@RequestParam("authentication") String authentication){
        try {
        return new ResponseEntity<>(accountHandler.getAccounts(authentication), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/status/update", produces = "application/json")
    public ResponseEntity<?> updateAccountStatus(@RequestParam("id") String id, @RequestParam("status") String status,@RequestParam("authentication") String authentication) throws AuthorizationException, GeneralServiceException {
        try {
        return new ResponseEntity<>(accountHandler.updateAccountStatus(id, AccountStatus.valueOf(status), authentication), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(path = "profile/picture/upload/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadProfilePicture(@PathVariable("id") String id, @RequestParam("file") MultipartFile file,@RequestParam("authentication") String authentication) {
        try{
        return new ResponseEntity<>(accountHandler.uploadProfilePicture(id, file,authentication),HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "service/link", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> linkAccountToService(@RequestBody @Valid AccountServiceLinkDTO accountServiceLinkDTO,@RequestParam("authentication") String authentication){
        try{
        return new ResponseEntity<>(accountHandler.linkAccountWithService(accountServiceLinkDTO.getAccountId(), accountServiceLinkDTO.getServiceId(),authentication), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(path = "enquiry/{msisdn}", produces = "application/json")
    public ResponseEntity<?> getAccountByMsisdn(@PathVariable("msisdn") String msisdn,@RequestParam("authentication") String authentication){
        try{
        return new ResponseEntity<>(accountHandler.enquiry(msisdn,authentication), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/transfer", produces = "application/json")
    public ResponseEntity<String> transfer(@RequestBody TransferDTO transferDTO,@RequestParam("authentication") String authentication){
        try{
        return new ResponseEntity<>(accountHandler.transfer(transferDTO,authentication), HttpStatus.OK); //,authentication
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(path = "/withdraw", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> withdraw(@RequestBody WithdrawDTO withdrawDTO,@RequestParam("authentication") String authentication){
        try {
        return new ResponseEntity<>(accountHandler.withdraw(withdrawDTO,authentication), HttpStatus.OK);
        } catch (Exception exception) {
       return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/payment/{beneficiaryId}/{externalTransactionId}", produces = "application/json")
    public ResponseEntity<String> payment(@PathVariable("beneficiaryId") String beneficiaryId, @PathVariable("externalTransactionId") String externalTransactionId,@RequestParam("authentication") String authentication){
        try {
        return new ResponseEntity<>(accountHandler.payment(beneficiaryId, externalTransactionId,authentication), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/payment/status/check/{externalTransactionId}", produces = "application/json")
    public ResponseEntity<?> getPaymentStatus(@PathVariable("externalTransactionId") String externalTransactionId,@RequestParam("authentication") String authentication){
        try{
        return new ResponseEntity<>(paystackServiceClient.verifyPayment(externalTransactionId), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "balance/update",consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updateBalances(@RequestBody List<Account> accounts,@RequestParam("authentication") String authentication){
        try {
        return new ResponseEntity<>(accountHandler.updateBalances(accounts,authentication), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
