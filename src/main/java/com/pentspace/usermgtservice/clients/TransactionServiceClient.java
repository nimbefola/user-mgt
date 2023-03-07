package com.pentspace.usermgtservice.clients;

import com.pentspace.usermgtservice.dto.Transaction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "TransactionServiceClient", url = "http://localhost:30305/transaction")
public interface TransactionServiceClient {
    @PostMapping( consumes = "application/json", produces = "application/json")
     Transaction create(@RequestBody Transaction transaction);

}
