package com.pentspace.usermgtservice.handlers;

import com.pentspace.usermgtservice.entities.Account;

import java.util.concurrent.ThreadLocalRandom;

public abstract class BaseHandler {

    protected String generateOTP(){
        int otp = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return String.valueOf(otp);
    }

}
