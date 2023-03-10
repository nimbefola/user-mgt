package com.pentspace.accountmgtservice.endpoints;

import com.pentspace.accountmgtservice.dto.*;
import com.pentspace.accountmgtservice.entities.Account;
import com.pentspace.accountmgtservice.entities.enums.AccountStatus;
import com.pentspace.accountmgtservice.handlers.AccountHandler;
import com.pentspace.accountmgtservice.clients.PaystackServiceClient;
import com.pentspace.usermgtservice.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping( produces = "application/json", consumes = "application/json")
    public ResponseEntity<Account> create(@RequestBody @Valid AccountDTO request) {
        return new ResponseEntity<>(accountHandler.createAccount(request), HttpStatus.OK);
    }

    @PostMapping(path = "/complete/registration", produces = "application/json")
    public ResponseEntity<Account> completeRegistration(@RequestBody RegistrationCompletionDTO request){
        return new ResponseEntity<>(accountHandler.activate(request.getAccountId(), request.getOtp()), HttpStatus.OK);
    }

    @PostMapping(path = "/auth", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Account> authenticateAccount(@RequestBody LoginDTO loginDTO){
        return new ResponseEntity<>(accountHandler.authenticateAccount(loginDTO.getUsername(), loginDTO.getPassword()), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Account> getById(@PathVariable("id") String id){
        return new ResponseEntity<>(accountHandler.getById(id), HttpStatus.OK);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Account>> getAll(){
        return new ResponseEntity<>(accountHandler.getAccounts(), HttpStatus.OK);
    }

    @PutMapping(path = "/status/update", produces = "application/json")
    public ResponseEntity<Account> updateAccountStatus(@RequestParam("id") String id, @RequestParam("status") String status){
        return new ResponseEntity<>(accountHandler.updateAccountStatus(id, AccountStatus.valueOf(status)), HttpStatus.OK);
    }

    @PostMapping(path = "profile/picture/upload/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> uploadProfilePicture(@PathVariable("id") String id, @RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(accountHandler.uploadProfilePicture(id, file),HttpStatus.OK);
    }

    @PostMapping(path = "service/link", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Account> linkAccountToService(@RequestBody @Valid AccountServiceLinkDTO accountServiceLinkDTO){
        return new ResponseEntity<>(accountHandler.linkAccountWithService(accountServiceLinkDTO.getAccountId(), accountServiceLinkDTO.getServiceId()), HttpStatus.OK);
    }

    @PutMapping(path = "/balance/debit", produces = "text/plain")
    public ResponseEntity<String> debitBalance(@RequestParam("id") String id, @RequestParam("amount") String amount){
        return new ResponseEntity<>(accountHandler.debitBalance(id,new BigDecimal(amount)), HttpStatus.OK);
    }

    @PutMapping(path = "/balance/credit", produces = "text/plain")
    public ResponseEntity<String> creditBalance(@RequestParam("id") String id, @RequestParam("amount") String amount){
        return new ResponseEntity<>(accountHandler.creditBalance(id,new BigDecimal(amount)), HttpStatus.OK);
    }

    @GetMapping(path = "enquiry/{msisdn}", produces = "application/json")
    public ResponseEntity<Account> getAccountByMsisdn(@PathVariable("msisdn") String msisdn){
        return new ResponseEntity<>(accountHandler.enquiry(msisdn), HttpStatus.OK);
    }

    @PostMapping(path = "/transfer", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> transfer(@RequestBody TransferDTO transferDTO){
        return new ResponseEntity<>(accountHandler.transfer(transferDTO), HttpStatus.OK);
    }

    @PostMapping(path = "/withdraw", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> withdraw(@RequestBody WithdrawDTO withdrawDTO){
        return new ResponseEntity<>(accountHandler.withdraw(withdrawDTO), HttpStatus.OK);
    }

    @PutMapping(path = "/payment/{beneficiaryId}/{externalTransactionId}", produces = "application/json")
    public ResponseEntity<String> payment(@PathVariable("beneficiaryId") String beneficiaryId, @PathVariable("externalTransactionId") String externalTransactionId){
        return new ResponseEntity<>(accountHandler.payment(beneficiaryId, externalTransactionId), HttpStatus.OK);
    }

    @GetMapping(path = "/deposit/status/check/{externalTransactionId}", produces = "application/json")
    public ResponseEntity<PaystackPaymentStatusDTO> getDepositStatus(@PathVariable("externalTransactionId") String externalTransactionId){
        return new ResponseEntity<>(paystackServiceClient.verifyPayment(externalTransactionId), HttpStatus.OK);
    }

}
