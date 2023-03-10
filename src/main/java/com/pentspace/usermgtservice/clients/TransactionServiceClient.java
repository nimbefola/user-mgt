package com.pentspace.usermgtservice.clients;

import com.pentspace.usermgtservice.dto.Transaction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "TransactionServiceClient", url = "http://localhost:30305/transaction")
public interface TransactionServiceClient {
    @PostMapping( consumes = "application/json", produces = "application/json")
    Transaction create(@RequestBody Transaction transaction);

    @GetMapping(path = "/externalTransaction/{externalTransactionId}", produces = "application/json")
    Transaction getBySourceId(@PathVariable("externalTransactionId") String externalTransactionId);
}
