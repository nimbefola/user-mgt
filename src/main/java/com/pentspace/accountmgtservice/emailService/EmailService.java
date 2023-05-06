package com.pentspace.accountmgtservice.emailService;


import com.pentspace.accountmgtservice.entities.Account;

import javax.mail.MessagingException;

public interface EmailService {

    void sendRegistrationSuccessfulEmail(Account account, String token)throws MessagingException;

   void sendVerificationMessage(Account account) throws MessagingException;

    void sendChangePasswordMessage(Account account) throws MessagingException;

    void sendForgotPasswordMessage(Account account, String token) throws MessagingException;

    void resetPasswordConfirmation(Account account) throws MessagingException;
//
//    void sendPaymentConfirmationMail(Account account) throws MessagingException;
}
