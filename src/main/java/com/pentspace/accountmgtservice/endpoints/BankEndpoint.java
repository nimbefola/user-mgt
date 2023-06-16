package com.pentspace.accountmgtservice.endpoints;

import com.pentspace.accountmgtservice.dto.BankDTO;
import com.pentspace.accountmgtservice.dto.BankDetailsDTO;
import com.pentspace.accountmgtservice.entities.Bank;
import com.pentspace.accountmgtservice.entities.BankDetail;
import com.pentspace.accountmgtservice.services.BankDetailService;
import com.pentspace.accountmgtservice.services.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "bank")
@CrossOrigin(origins = "*")
public class BankEndpoint {
    @Autowired
    private BankService bankService;

    @Autowired
    private BankDetailService bankDetailService;

    @PostMapping("/createBank")
    public ResponseEntity<Bank> createBank(BankDTO bank){
        return new ResponseEntity<>(bankService.createBank(bank),HttpStatus.OK);
    }

    public ResponseEntity<BankDetail> addBankDetails(BankDetailsDTO bankDetailsDTO){
        return new ResponseEntity<>(bankDetailService.addBankDetail(bankDetailsDTO),HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Bank> getById(@PathVariable("id") String bankId){
        return new ResponseEntity<>(bankService.getBankById(bankId), HttpStatus.OK);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Bank>> getAll(){
        return new ResponseEntity<>(bankService.getAllBank(), HttpStatus.OK);
    }
}
