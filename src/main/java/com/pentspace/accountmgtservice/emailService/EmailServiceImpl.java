package com.pentspace.accountmgtservice.emailService;


import com.pentspace.accountmgtservice.entities.Account;
import com.pentspace.accountmgtservice.entities.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;


@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;


    private final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    private final String REGISTRATION_ERROR_MESSAGE = "Registration Not Successful";

    private final String FORGOT_PASSWORD_ERROR_MESSAGE = "Password Recovery Not Successful";

    private final String MESSAGE_NOT_SENT_MESSAGE = "Message Not sent from enmasse";

    private final String SCHEDULE_NOT_SENT= "Schedule not sent! Kindly retry";



    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendRegistrationSuccessfulEmail(User user, String token) throws MessagingException {
        String link = "http://localhost:9090" + token;

        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Account Activation");
        simpleMailMessage.setFrom("PENTSPACE");
        String template = "Dear [[name]],\n"
                + "Thanks for registering on Pentspace.\n"
                + "Kindly use the code below to validate your account and activate your account:\n"
                + "[[code]]\n"
                + "Thank you.\n"
                + "Pent space team";
        template = template.replace("[[name]]", user.getFirstName());
        template = template.replace("[[code]]", token);
        simpleMailMessage.setText(template);

        try {
            javaMailSender.send(simpleMailMessage);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new MessagingException(String.format(REGISTRATION_ERROR_MESSAGE));
        }
    }

    @Override
    public void sendVerificationMessage(User user) throws MessagingException {

        SimpleMailMessage message = new SimpleMailMessage();
        simpleMailMessage.setText("PENTSPACE");

        simpleMailMessage.setFrom("PENTSPACE");
        //simpleMailMessage.setTo(usersEntity.getEmail());
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Welcome to Pentspace");
        String template = "Dear [[name]],\n"
                + "Your account has been verified, kindly login to explore\n"
                + "[[URL]]\n"
                + "Thank you,\n\n"
                + "The Pentspace Team";
        //    template = template.replace("[[name]]", usersEntity.getFirstName());
        template = template.replace("[[name]]", user.getFirstName());
        template = template.replace("[[URL]]", "Pentspace");
        // message.setText(template);
        simpleMailMessage.setText(template);
        try {
            javaMailSender.send(simpleMailMessage);
        } catch (Exception exception) {
            throw new MessagingException(MESSAGE_NOT_SENT_MESSAGE);
        }
    }

    @Override
    public void sendChangePasswordMessage(User user) throws MessagingException {

        SimpleMailMessage message = new SimpleMailMessage();
        simpleMailMessage.setText("Pentspace");

        simpleMailMessage.setFrom("PENTSPACE");

        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Change Password");
        String template = "Dear [[name]],\n"
                + "You have successfully change your password!"
                + " Kindly reach out to us, if you have any incoherence from your side \n"
                + "[[URL]]\n"
                + "Thank you,\n\n"
                + "The Pentspace Team";
        template = template.replace("[[name]]", user.getFirstName());
        template = template.replace("[[URL]]", "Pentspace");
        simpleMailMessage.setText(template);
        try {

            javaMailSender.send(simpleMailMessage);
        } catch (Exception exception) {
            throw new MessagingException(MESSAGE_NOT_SENT_MESSAGE);
        }
    }

    @Override
    public void sendForgotPasswordMessage(User user, String token) throws MessagingException {
        String link = "http://localhost:9090" + token;
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Password Recovery");
        simpleMailMessage.setFrom("PENTSPACE");
        String template = "Dear [[name]],\n"
                + "Thanks for using Pentspace.\n"
                + "Kindly use the code below to validate your account, and recover your account:\n"
                + "[[code]]\n"
                + "Thank you.\n"
                + "Pentspace team";
        template = template.replace("[[name]]", user.getFirstName());
        template = template.replace("[[code]]", token);
        simpleMailMessage.setText(template);

        try {
            javaMailSender.send(simpleMailMessage);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new MessagingException(String.format(FORGOT_PASSWORD_ERROR_MESSAGE));
        }
    }

    @Override
    public void resetPasswordConfirmation(User user) throws MessagingException {

        SimpleMailMessage message = new SimpleMailMessage();
        simpleMailMessage.setText("PENTSPACE");

        simpleMailMessage.setFrom("PENTSPACE");
        //simpleMailMessage.setTo(usersEntity.getEmail());
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Success!!!");
        String template = "Dear [[name]],\n"
                + "Your account has been re-activated, kindly login to explore\n"
                + "[[URL]]\n"
                + "Thank you,\n\n"
                + "The Pentspace Team";
        template = template.replace("[[name]]", user.getFirstName());
        template = template.replace("[[URL]]", "Pentspace");
        // message.setText(template);
        simpleMailMessage.setText(template);
        try {
            javaMailSender.send(simpleMailMessage);
        } catch (Exception exception) {
            throw new MessagingException(MESSAGE_NOT_SENT_MESSAGE);
        }
    }


//    @Override
//    public void sendPaymentConfirmationMail(UsersEntity usersEntity) throws MessagingException {
//
//        simpleMailMessage.setTo(usersEntity.getEmail());
//        simpleMailMessage.setSubject("Transaction Successful");
//        simpleMailMessage.setFrom("salamikehinde345@gmail.com");
//        String template = "Dear [[name]],\n"
//                + "You have successfully pay for your goods\n"
//                + "Thank you.\n"
//                + "En Masse team";
//        template = template.replace("[[name]]", usersEntity.getFirstname());
//        simpleMailMessage.setText(template);
//
//        try {
//            javaMailSender.send(simpleMailMessage);
//        } catch (Exception exception) {
//            exception.printStackTrace();
//            throw new MessagingException(String.format(SCHEDULE_NOT_SENT));
//        }
//    }
//
//
//

}


