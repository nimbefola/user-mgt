package com.pentspace.accountmgtservice.handlers.impl;

import com.pentspace.accountmgtservice.handlers.HashManagerHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
