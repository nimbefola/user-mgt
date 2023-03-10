package com.pentspace.accountmgtservice.clients;

import com.pentspace.accountmgtservice.dto.PaystackPaymentStatusDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "PaystackServiceClient", url = "https://api.paystack.co/transaction/verify") // we can externalise this
public interface PaystackServiceClient {

    @GetMapping( path = "/{reference}", consumes = "application/json", produces = "application/json")
    PaystackPaymentStatusDTO verifyPayment(@PathVariable("reference") String reference);

}
