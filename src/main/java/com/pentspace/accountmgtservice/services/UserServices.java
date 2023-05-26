package com.pentspace.accountmgtservice.services;

import com.pentspace.accountmgtservice.dto.*;
import com.pentspace.accountmgtservice.exceptions.*;

import javax.mail.MessagingException;


public interface UserServices {

   UserSignUpResponseDto signUp(UserSignUpRequestDto userSignUpRequestDto) throws MessagingException, GeneralServiceException, AccountCreationException;

    ValidateDto validateAccount(ValidateDto validateDto) throws GeneralServiceException, MessagingException;

    LoginResponseDto login(LoginDTO loginDTO) throws GeneralServiceException, IncorrectPasswordException;

    ChangePasswordDTO changePassword(ChangePasswordDTO changePasswordDTO, String authentication) throws AuthorizationException, GeneralServiceException, MessagingException;

    ForgotPasswordDTO forgotPassword(ForgotPasswordDTO forgotPasswordDTO) throws GeneralServiceException, MessagingException;

    RetrieveForgotPasswordDTO retrieveForgottenPassword(RetrieveForgotPasswordDTO retrieveForgotPasswordDTO) throws MessagingException, GeneralServiceException;


}
