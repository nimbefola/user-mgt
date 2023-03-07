package com.pentspace.usermgtservice.endpoints;

import com.pentspace.usermgtservice.dto.AccountDTO;
import com.pentspace.usermgtservice.dto.AccountServiceLinkDTO;
import com.pentspace.usermgtservice.dto.LoginDTO;
import com.pentspace.usermgtservice.dto.RegistrationCompletionDTO;
import com.pentspace.usermgtservice.entities.Account;
import com.pentspace.usermgtservice.entities.enums.AccountStatus;
import com.pentspace.usermgtservice.handlers.AccountHandler;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@CrossOrigin(origins = "*")
public class AccountEndpoint {
    @Autowired
    private AccountHandler accountHandler;

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

    @PutMapping(path = "/transfer", produces = "text/plain")
    public ResponseEntity<String> transfer(@RequestParam("sourceId") String sourceId, @RequestParam("beneficiaryId") String beneficiaryId, @RequestParam("amount") String amount){
        return new ResponseEntity<>(accountHandler.transfer(sourceId, beneficiaryId, amount), HttpStatus.OK);
    }

    @PutMapping(path = "/withdraw", produces = "text/plain")
    public ResponseEntity<String> withdraw(@RequestParam("beneficiaryId") String beneficiaryId, @RequestParam("amount") String amount){
        return new ResponseEntity<>(accountHandler.withdraw(beneficiaryId, amount), HttpStatus.OK);
    }

    @PutMapping(path = "/deposit/", produces = "text/plain")
    public ResponseEntity<String> deposit(@RequestParam("beneficiaryId") String beneficiaryId, @RequestParam("externalTransactionId") String externalTransactionId){
       // return new ResponseEntity<>(accountHandler.deposit(beneficiaryId, externalTransactionId), HttpStatus.OK);
        return new ResponseEntity<>(" Awaiting Implementation ", HttpStatus.OK);
    }

}
