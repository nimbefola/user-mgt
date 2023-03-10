package com.pentspace.usermgtservice.clients;

import com.pentspace.usermgtservice.dto.PaystackPaymentStatusDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "PaystackServiceClient", url = "https://api.paystack.co/transaction/verify") // we can externalise this
public interface PaystackServiceClient {

    @GetMapping( path = "/{reference}", consumes = "application/json", produces = "application/json")
    PaystackPaymentStatusDTO verifyPayment(@PathVariable("reference") String reference);

}
