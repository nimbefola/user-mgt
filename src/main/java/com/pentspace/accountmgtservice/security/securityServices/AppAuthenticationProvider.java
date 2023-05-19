package com.pentspace.accountmgtservice.security.securityServices;


import com.pentspace.accountmgtservice.entities.Account;
import com.pentspace.accountmgtservice.entities.User;
import com.pentspace.accountmgtservice.entities.enums.AccountType;
import com.pentspace.accountmgtservice.entities.enums.Roles;
import com.pentspace.accountmgtservice.entities.repositories.AccountRepository;
import com.pentspace.accountmgtservice.entities.repositories.UserRepository;
import com.pentspace.accountmgtservice.exceptions.ApplicationExceptionHandler;
import com.pentspace.accountmgtservice.exceptions.AuthorizationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Primary
public class AppAuthenticationProvider implements AuthenticationManager {

    @Autowired
    UserRepository userRepository;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        //String username = token.getName();
        String name = token.getName();
        String password = (String) token.getCredentials();



        Optional<User> user = userRepository.findUserByEmail(name);

        if (!user.isPresent()) {
            throw new BadCredentialsException("There is not account with given credentials");
        }


        //UsersEntity usersEntity=user.get();
        User userEntity = user.get();
        List<Roles> authorities = Collections.singletonList(userEntity.getRoles());
        if(userEntity.getRoles() == null) {
            try {
                throw new AuthorizationException("User has no authority");
            } catch (AuthorizationException e) {
                e.printStackTrace();
            }
        }
        return new UsernamePasswordAuthenticationToken(name, password, authorities.stream().map(authority
                -> new SimpleGrantedAuthority(authority.name())).collect(Collectors.toList()));
    }

}
