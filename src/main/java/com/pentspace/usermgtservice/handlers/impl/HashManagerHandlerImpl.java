package com.pentspace.usermgtservice.handlers.impl;

import com.pentspace.usermgtservice.handlers.HashManagerHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

@Service
public class HashManagerHandlerImpl implements HashManagerHandler {
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Override
    public String hashData(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    @Override
    public boolean validateData(String passwordPlainText, String databasePassword) {
        return bCryptPasswordEncoder.matches(passwordPlainText,databasePassword);
    }


}
