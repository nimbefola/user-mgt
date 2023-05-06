package com.pentspace.accountmgtservice.security.securityServices;

import com.pentspace.accountmgtservice.dto.AccountDTO;
import com.pentspace.accountmgtservice.dto.LoginDTO;
import com.pentspace.accountmgtservice.entities.Account;
import com.pentspace.accountmgtservice.entities.repositories.AccountRepository;
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
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private AppAuthenticationProvider authenticationManager;

    @Autowired
    TokenProviderServiceImpl tokenProviderService;


    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

        Optional<Account> optionalUser = accountRepository.findByEmail(name);
        if(!optionalUser.isPresent()){
            throw new UsernameNotFoundException("User with given email not found");
        }
        else{
            Account account =  optionalUser.get();
            return ApplicationUser.create(account);
        }
    }


    public JWTToken loginUser(LoginDTO loginDTO) throws UsernameNotFoundException, IncorrectPasswordException, GeneralServiceException {
        Optional<Account> account = accountRepository.findByEmail(loginDTO.getEmail());


        if(account.isPresent()){
            if(!account.get().getEnabled()){
                throw new GeneralServiceException("Account has not been enabled");
            }
            boolean matchingResult=passwordEncoder.matches(loginDTO.getPassword(), account.get().getPassword());

            if(!matchingResult){
                throw new IncorrectPasswordException("The password is Incorrect");
            }
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(), loginDTO.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            account = accountRepository.findByEmail(loginDTO.getEmail());

            JWTToken jwtToken = new JWTToken(tokenProviderService.generateLoginToken(authentication, account.get()));

            return jwtToken;
        }
        throw new UsernameNotFoundException("User Not Found");
    }

    public String signUpUser(Account account) {
        StringBuilder stringBuilder= new StringBuilder("Validates ");
        boolean userExists=accountRepository.findByEmail(account.getEmail()).isPresent();
        if(userExists){
            throw new IllegalStateException("user with this email already exists");
        }
        accountRepository.save(account);
        String encodedPassword=passwordEncoder.encode(account.getPassword());
        account.setPassword(encodedPassword);
        String token= UUID.randomUUID().toString();

        stringBuilder.append(token);
        return stringBuilder.toString();
    }

    public String sendRegistrationToken(Account account){
        //mailsender
        String token= UUID.randomUUID().toString().replace("-","").substring(0,6);
        return token;
    }


    public String getUserEmailAddressFromToken(String token) throws AuthorizationException {
        return tokenProviderService.getEmailFromToken(token);
    }

    public boolean passwordMatches(String password,String password2){
       return passwordEncoder.matches(password, password2);
    }

}
