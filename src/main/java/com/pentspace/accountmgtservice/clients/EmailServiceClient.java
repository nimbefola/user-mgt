//package com.pentspace.accountmgtservice.clients;
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.*;
//
//@FeignClient(value = "EmailClient", url = "http://localhost:30302/notification")
//        //not comfy calling this host website obviously not working
//public interface EmailServiceClient {
//    @PostMapping( consumes = "application/json", produces = "application/json")
//    String sendEmail(@RequestParam("email") String email, @RequestParam("otp") String otp, @RequestParam("title") String title);
//
//}


// "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYWxhbXRheWUwQGdtYWlsLmNvbSIsImFjY291bnR5cGUiOiJTRVJWSUNFX1BST1ZJREVSIiwiaXNzIjoicGVudHNwYWNlIiwiaWF0IjoxNjgyNjQ5MDQ0LCJleHAiOjE2ODI3MzU0NDR9.PTAbGL4iK74aQOVgTrwZ1a4PfcD1MosCl_mZLHIKdYpsUzbWgvdtgWOrSiPaZeFME9w2A0a9UCigrkoHWsMHNA",
//    "tokenType": "BEARER_TOKEN"

//Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmR1bHNhbGFtZXN0aGVyMzQ1QGdtYWlsLmNvbSIsImFjY291bnR5cGUiOiJTRVJWSUNFX1BST1ZJREVSIiwiaXNzIjoicGVudHNwYWNlIiwiaWF0IjoxNjgyNzIxODgxLCJleHAiOjE2ODI4MDgyODF9.h9NxWNZUVYK4Lts7b2ZWfOSptD6jtyleocfZnHEC95WIWEjV3X2ueXg4sEX37qGVQBRPTtHuICiOCsbv83xZCw