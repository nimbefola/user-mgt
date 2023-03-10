package com.pentspace.accountmgtservice.clients;

import com.pentspace.accountmgtservice.dto.Transaction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "TransactionServiceClient", url = "http://localhost:30305/transaction")
public interface TransactionServiceClient {
    @PostMapping( consumes = "application/json", produces = "application/json")
    Transaction create(@RequestBody Transaction transaction);

    @GetMapping(path = "/externalTransaction/{externalTransactionId}", produces = "application/json")
    Transaction getBySourceId(@PathVariable("externalTransactionId") String externalTransactionId);
}
