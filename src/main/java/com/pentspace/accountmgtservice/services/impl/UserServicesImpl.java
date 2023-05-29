package com.pentspace.accountmgtservice.services.impl;

import com.pentspace.accountmgtservice.dto.*;
import com.pentspace.accountmgtservice.emailService.EmailService;
import com.pentspace.accountmgtservice.entities.User;
import com.pentspace.accountmgtservice.entities.enums.Roles;
import com.pentspace.accountmgtservice.entities.repositories.UserRepository;
import com.pentspace.accountmgtservice.exceptions.*;
import com.pentspace.accountmgtservice.security.securityServices.UserPrincipalService;
import com.pentspace.accountmgtservice.security.securityUtils.JWTToken;
import com.pentspace.accountmgtservice.serviceUtil.IdGenerator;
import com.pentspace.accountmgtservice.serviceUtil.StringUtil;
import com.pentspace.accountmgtservice.services.UserServices;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.mail.MessagingException;
import java.util.Optional;

@Slf4j
@Service
public class UserServicesImpl implements UserServices {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserPrincipalService userPrincipalService;

    @Autowired
    EmailService emailService;


    @Autowired
    ModelMapper modelMapper;

    @Override
    public UserSignUpResponseDto signUp(UserSignUpRequestDto userSignUpRequestDto) throws MessagingException, AccountCreationException, GeneralServiceException {
        checkAllParameters(userSignUpRequestDto);

        userWithEmailExists(userSignUpRequestDto);

        User users = createUserEntityFromDetails(userSignUpRequestDto);

        String confirmation = userPrincipalService.sendRegistrationToken(users);

        users.setValidationToken(confirmation);


        emailService.sendRegistrationSuccessfulEmail(users, confirmation);

        userRepository.save(users);

        log.info(confirmation.toString());

        return generateRegistrationResponse(userSignUpRequestDto, users.getId());
    }

    @Override
    public ValidateDto validateAccount(ValidateDto validateDto) throws GeneralServiceException, MessagingException {

        if(StringUtil.isBlank(validateDto.getEmail())){
            throw new GeneralServiceException("Email Cannot be empty");
        }

        Optional<User> users = userRepository.findUserByEmail(validateDto.getEmail());

        if (!users.isPresent()){
            throw new GeneralServiceException("User With this email does not exist");
        }

            if (users.get().getValidationToken().equals(validateDto.getToken())) {
                log.info("its working");
                users.get().setEnabled(true);
                users.get().setValidationToken(null );

                userRepository.save(users.get());

                return validateDto;
            }
                throw new GeneralServiceException("Token not valid or already used");
            }


    @Override
    public LoginResponseDto login(LoginDTO loginDTO) throws GeneralServiceException, IncorrectPasswordException {
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        JWTToken jwtToken = userPrincipalService.loginUser(loginDTO);
        if (jwtToken != null) {
            Optional<User> users = userRepository.findUserByEmail(loginDTO.getEmail());
            if (users.isPresent()) {
                User user = users.get();
                if (!user.getEnabled()) {
                    throw new GeneralServiceException("User account has not been activated");
                }
            }
            users.ifPresent(entity -> modelMapper.map(entity, loginResponseDto));
            loginResponseDto.setToken(jwtToken);
            log.info(jwtToken.toString());
        }
        log.info(loginResponseDto.toString());
        return loginResponseDto;
    }


    @Override
    public ChangePasswordDTO changePassword(ChangePasswordDTO changePasswordDTO, String authentication) throws AuthorizationException, MessagingException, UsernameNotFoundException, GeneralServiceException {
        String userEmail = userPrincipalService.getUserEmailAddressFromToken(authentication);


        Optional<User> user = userRepository.findUserByEmail(userEmail);
        if (!user.isPresent()) {
            throw new GeneralServiceException("User not found");
        }

        boolean matches = userPrincipalService.passwordMatches(
                changePasswordDTO.getOldPassword(), user.get().getPassword());


        if (!matches) {
            throw new GeneralServiceException("Old password is incorrect");
        } else
            user.get().setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));

        userRepository.save(user.get());

        emailService.sendChangePasswordMessage(user.get());

        return changePasswordDTO;
    }

    @Override
    public ForgotPasswordDTO forgotPassword(ForgotPasswordDTO forgotPasswordDTO) throws MessagingException, UsernameNotFoundException {

        Optional<User> user = userRepository.findUserByEmail(forgotPasswordDTO.getEmail());
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("No such user exists!");
        }

        String confirmation = userPrincipalService.sendRegistrationToken(user.get());

        user.get().setValidationToken(confirmation);

        userRepository.save(user.get());

        emailService.sendForgotPasswordMessage(user.get(), confirmation);
        return forgotPasswordDTO;
    }


    @Override
    public RetrieveForgotPasswordDTO retrieveForgottenPassword(RetrieveForgotPasswordDTO retrieveForgotPasswordDTO) throws MessagingException, GeneralServiceException, UsernameNotFoundException {

        if(StringUtil.isBlank(retrieveForgotPasswordDTO.getEmail())){
            throw new GeneralServiceException("Email Cannot be empty");
        }

        Optional<User> users = userRepository.findUserByEmail(retrieveForgotPasswordDTO.getEmail());
        if (!users.isPresent()) {
            throw new GeneralServiceException("User With this email does not exist");
        }

            if (users.get().getValidationToken().equals(retrieveForgotPasswordDTO.getToken())) {
                users.get().setEnabled(true);
                users.get().setValidationToken(null);

                users.get().setPassword(passwordEncoder.encode(retrieveForgotPasswordDTO.getNewPassword()));

                userRepository.save(users.get());

                emailService.resetPasswordConfirmation(users.get());
                return retrieveForgotPasswordDTO;
            }
                throw new GeneralServiceException("Token not valid or already used");
            }


    private void checkAllParameters(UserSignUpRequestDto userSignUpRequestDto) throws AccountCreationException {

        if(StringUtil.isBlank(userSignUpRequestDto.getFirstName())){
            throw new AccountCreationException("FirstName Cannot be empty");
        }
        if(StringUtil.isBlank(userSignUpRequestDto.getLastName())){
            throw new AccountCreationException("LastName Cannot be empty");
        }
        if(StringUtil.isBlank(userSignUpRequestDto.getPhoneNumber())){
            throw new AccountCreationException("Phone number Cannot be empty");
        }
        if(StringUtil.isBlank(userSignUpRequestDto.getCountry())){
            throw new AccountCreationException("Country Cannot be empty");
        }
        if(StringUtil.isBlank(userSignUpRequestDto.getEmail())){
            throw new AccountCreationException("Email Cannot be empty");
        }
        if(StringUtil.isBlank(userSignUpRequestDto.getPassword())){
            throw new AccountCreationException("Password Cannot be empty");
        }
    }

    private User createUserEntityFromDetails(UserSignUpRequestDto userSignUpRequestDto){
        User user= new User();
        modelMapper.map(userSignUpRequestDto,user);
        String password=passwordEncoder.encode(userSignUpRequestDto.getPassword());
        user.setPassword(password);
        user.setId(IdGenerator.generateId());
        user.setRoles(Roles.BASE_USER);
        return user;
    }


    private UserSignUpResponseDto generateRegistrationResponse(UserSignUpRequestDto userSignUpRequestDto, String id){
        UserSignUpResponseDto userSignUpResponseDto=new UserSignUpResponseDto();

        modelMapper.map(userSignUpRequestDto,userSignUpResponseDto);

        userSignUpResponseDto.setId(id);
        userSignUpResponseDto.setRole(Roles.BASE_USER);

        return userSignUpResponseDto;
    }
    private void userWithEmailExists(UserSignUpRequestDto userSignUpRequestDto) throws GeneralServiceException {

        Optional<User> userByEmail = userRepository.findUserByEmail(userSignUpRequestDto.getEmail());
        if(userByEmail.isPresent()){
            throw new GeneralServiceException("Users with this email already exists");
        }

    }

}
