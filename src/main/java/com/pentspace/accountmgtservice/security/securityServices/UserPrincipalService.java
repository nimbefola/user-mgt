package com.pentspace.accountmgtservice.security.securityServices;

import com.pentspace.accountmgtservice.dto.LoginDTO;
import com.pentspace.accountmgtservice.dto.ValidateDto;
import com.pentspace.accountmgtservice.entities.User;
import com.pentspace.accountmgtservice.entities.repositories.UserRepository;
import com.pentspace.accountmgtservice.exceptions.AuthorizationException;
import com.pentspace.accountmgtservice.exceptions.GeneralServiceException;
import com.pentspace.accountmgtservice.exceptions.IncorrectPasswordException;
import com.pentspace.accountmgtservice.security.securityUtils.JWTToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserPrincipalService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private AppAuthenticationProvider authenticationManager;

    @Autowired
    TokenProviderServiceImpl tokenProviderService;


    @Override
    public UserDetails loadUserByUsername(String firstName) throws UsernameNotFoundException {

        Optional<User> optionalUser = userRepository.findUserByEmail(firstName);
        if(!optionalUser.isPresent()){
            throw new UsernameNotFoundException("User with given email not found");
        }
        else{
            User user =  optionalUser.get();
            return ApplicationUser.create(user);
        }
    }


    public JWTToken loginUser(LoginDTO loginDTO) throws UsernameNotFoundException, IncorrectPasswordException, GeneralServiceException {
         Optional<User> user = userRepository.findUserByEmail(loginDTO.getEmail());


        if(user.isPresent()){
           if(!user.get().getEnabled()){
                throw new GeneralServiceException("Account has not been enabled");
            }
            boolean matchingResult=passwordEncoder.matches(loginDTO.getPassword(), user.get().getPassword());

            if(!matchingResult){
                throw new IncorrectPasswordException("The password is Incorrect");
            }
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(), loginDTO.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            user = userRepository.findUserByEmail(loginDTO.getEmail());

            JWTToken jwtToken = new JWTToken(tokenProviderService.generateLoginToken(authentication, user.get()));

            return jwtToken;
        }
        throw new UsernameNotFoundException("User Not Found");
    }

    public JWTToken signUpUser(ValidateDto validateDto) throws UsernameNotFoundException, IncorrectPasswordException, GeneralServiceException {
        Optional<User> optionalUser = userRepository.findUserByEmail(validateDto.getEmail());


            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            validateDto.getEmail(), validateDto.getToken()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);


            JWTToken jwtToken = new JWTToken(tokenProviderService.generateLoginToken(authentication, optionalUser.get()));

            return jwtToken;
        }


    public String signUpUser(User user) {
        StringBuilder stringBuilder= new StringBuilder("Validates ");
        boolean userExists=userRepository.findUserByEmail(user.getEmail()).isPresent();
        if(userExists){
            throw new IllegalStateException("user with this email already exists");
        }
        userRepository.save(user);
        String encodedPassword=passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        String token= UUID.randomUUID().toString();

        stringBuilder.append(token);
        return stringBuilder.toString();
    }

    public String sendRegistrationToken(User user){
        //mailsender
        String token= UUID.randomUUID().toString().replace("-","").substring(0,4);
        return token;
    }


    public String getUserEmailAddressFromToken(String token) throws AuthorizationException {
        return tokenProviderService.getEmailFromToken(token);
    }

    public boolean passwordMatches(String password,String password2){
       return passwordEncoder.matches(password, password2);
    }

}
