package com.pentspace.accountmgtservice.exceptions;


import com.pentspace.accountmgtservice.response.ApiErrorResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.mail.MessagingException;
import java.util.*;

import static org.springframework.http.ResponseEntity.status;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();


        Map<String, Object> response = new HashMap<>();
        response.put("message", errorMessage);
        response.put("status", HttpStatus.BAD_REQUEST.value());

        return response;
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Map<String, Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Data integrity violation occurred: " + ex.getMessage());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSuchElementException.class)
    public Map<String, Object> handleNoSuchElementException(NoSuchElementException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "No such element found: " +  ex.getMessage());
        response.put("status", HttpStatus.NOT_FOUND.value());
        return response;
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public Map<String, Object> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "An unexpected error occurred: " + ex.getMessage());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return response;
    }


@ExceptionHandler(MessagingException.class)
    public final ResponseEntity<ApiErrorResponse> handleMessagingExceptions(MessagingException messagingException) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(messagingException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthorizationException.class)
    public final ResponseEntity<ApiErrorResponse> handleAuthorizationException(AuthorizationException authorizationException) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(authorizationException.getMessage(),HttpStatus.UNAUTHORIZED.value());

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(GeneralServiceException.class)
    public final ResponseEntity<ApiErrorResponse> handleGeneralException(GeneralServiceException generalServiceException) {

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(generalServiceException.getMessage(),HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountCreationException.class)
    public final ResponseEntity<ApiErrorResponse> handleAccountCreationException(AccountCreationException accountCreationException) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(accountCreationException.getMessage(), HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public final ResponseEntity<ApiErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException usernameNotFoundException) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(usernameNotFoundException.getMessage(), HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public final ResponseEntity<ApiErrorResponse> handleIncorrectPasswordException(IncorrectPasswordException incorrectPasswordException) {

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(incorrectPasswordException.getMessage(), HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public final ResponseEntity<ApiErrorResponse> handleIncorrectPasswordException(BadCredentialsException badCredentialsException) {

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(badCredentialsException.getMessage(), HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SecurityException.class)
    public final ResponseEntity<ApiErrorResponse> handleIncorrectPasswordException(SecurityException securityException) {

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(securityException.getMessage(), HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

}
