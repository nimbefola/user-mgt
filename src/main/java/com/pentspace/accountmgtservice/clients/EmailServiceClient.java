package com.pentspace.accountmgtservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "EmailClient", url = "http://localhost:30302/notification")
public interface EmailServiceClient {
    @PostMapping( consumes = "application/json", produces = "application/json")
    String sendEmail(@RequestParam("email") String email, @RequestParam("otp") String otp, @RequestParam("title") String title);

}
