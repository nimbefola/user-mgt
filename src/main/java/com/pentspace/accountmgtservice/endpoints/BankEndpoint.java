package com.pentspace.accountmgtservice.endpoints;

import com.pentspace.accountmgtservice.entities.Bank;
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

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Bank> getById(@PathVariable("id") String bankId){
        return new ResponseEntity<>(bankService.getBankById(bankId), HttpStatus.OK);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Bank>> getAll(){
        return new ResponseEntity<>(bankService.getAllBank(), HttpStatus.OK);
    }
}
