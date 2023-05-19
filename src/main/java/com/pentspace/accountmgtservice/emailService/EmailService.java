package com.pentspace.accountmgtservice.emailService;


import com.pentspace.accountmgtservice.entities.Account;
import com.pentspace.accountmgtservice.entities.User;

import javax.mail.MessagingException;

public interface EmailService {

    void sendRegistrationSuccessfulEmail(User user, String token)throws MessagingException;

   void sendVerificationMessage(User user) throws MessagingException;

    void sendChangePasswordMessage(User user) throws MessagingException;

    void sendForgotPasswordMessage(User user, String token) throws MessagingException;

    void resetPasswordConfirmation(User user) throws MessagingException;
//
//    void sendPaymentConfirmationMail(Account account) throws MessagingException;
}
