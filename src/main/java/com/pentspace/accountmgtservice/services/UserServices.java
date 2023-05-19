package com.pentspace.accountmgtservice.services;

import com.pentspace.accountmgtservice.dto.*;
import com.pentspace.accountmgtservice.entities.User;
import com.pentspace.accountmgtservice.exceptions.AccountCreationException;
import com.pentspace.accountmgtservice.exceptions.AuthorizationException;
import com.pentspace.accountmgtservice.exceptions.GeneralServiceException;
import com.pentspace.accountmgtservice.exceptions.IncorrectPasswordException;

import javax.mail.MessagingException;

public interface UserServices {

   UserSignUpResponseDto signUp(UserSignUpRequestDto userSignUpRequestDto) throws MessagingException, GeneralServiceException, AccountCreationException;

    boolean validateAccount(ValidateDto validateDto) throws GeneralServiceException, MessagingException;

    LoginResponseDto login(LoginDTO loginDTO) throws IncorrectPasswordException, GeneralServiceException;

    boolean changePassword(ChangePasswordDTO changePasswordDTO, String authentication) throws AuthorizationException, GeneralServiceException, MessagingException;

    boolean forgotPassword(ForgotPasswordDTO forgotPasswordDTO) throws GeneralServiceException, MessagingException;

    boolean retrieveForgottenPassword(RetrieveForgotPasswordDTO retrieveForgotPasswordDTO) throws MessagingException, GeneralServiceException;

}
