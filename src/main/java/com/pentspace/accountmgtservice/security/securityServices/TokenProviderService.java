package com.pentspace.accountmgtservice.security.securityServices;


import com.pentspace.accountmgtservice.entities.Account;
import com.pentspace.accountmgtservice.entities.User;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;


public interface TokenProviderService {
    String generateLoginToken(Authentication authentication, User user);


    String getEmailFromToken(String token);

    <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver);

    public UsernamePasswordAuthenticationToken getAuthentication(final String authenticationToken, final Authentication authentication, final UserDetails userDetails);

    boolean validateToken(String token, UserDetails userDetails);
}