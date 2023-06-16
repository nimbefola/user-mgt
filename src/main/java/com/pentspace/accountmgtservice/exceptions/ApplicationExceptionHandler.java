package com.pentspace.accountmgtservice.exceptions;

import com.pentspace.accountmgtservice.response.ApiErrorResponse;
import feign.FeignException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleException(MethodArgumentNotValidException exception){
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->{
            errorMap.put(error.getField(), error.getDefaultMessage());
        } );
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({DataIntegrityViolationException.class})
    public Map<String, String> handleDuplicateConstrain(DataIntegrityViolationException exception){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", exception.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSuchElementException.class)
    public Map<String, String> handleDuplicateConstrain(NoSuchElementException exception){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", exception.getMessage());
        return errorMap;
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(GeneralServiceException.class)
//    public Map<String, String> handleDuplicateConstrain(GeneralServiceException exception){
//        Map<String, String> errorMap = new HashMap<>();
//        errorMap.put("error", exception.getMessage());
//        return errorMap;
//    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public Map<String, String> handleDuplicateConstrain(RuntimeException exception){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", exception.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FeignException.class)
    public Map<String, String> handleDuplicateConstrain(FeignException exception){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", exception.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EnumConstantNotPresentException.class)
    public Map<String, String> handleDuplicateConstrain(EnumConstantNotPresentException exception){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", exception.getMessage());
        return errorMap;
    }


    @ExceptionHandler(MessagingException.class)
    public final ResponseEntity<ApiErrorResponse> handleMessagingExceptions(MessagingException messagingException) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(messagingException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,"error");

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthorizationException.class)
    public final ResponseEntity<ApiErrorResponse> handleAuthorizationException(AuthorizationException authorizationException) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(authorizationException.getMessage(),HttpStatus.UNAUTHORIZED,"error");

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(GeneralServiceException.class)
    public final ResponseEntity<ApiErrorResponse> handleGeneralException(GeneralServiceException generalServiceException) {

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(generalServiceException.getMessage(),HttpStatus.BAD_REQUEST,"error");

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountCreationException.class)
    public final ResponseEntity<ApiErrorResponse> handleAccountCreationException(AccountCreationException accountCreationException) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(accountCreationException.getMessage(), HttpStatus.BAD_REQUEST,"error");

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public final ResponseEntity<ApiErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException usernameNotFoundException) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(usernameNotFoundException.getMessage(), HttpStatus.NOT_FOUND,"error");

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public final ResponseEntity<ApiErrorResponse> handleIncorrectPasswordException(IncorrectPasswordException incorrectPasswordException) {

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(incorrectPasswordException.getMessage(), HttpStatus.BAD_REQUEST,"error");

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public final ResponseEntity<ApiErrorResponse> handleIncorrectPasswordException(BadCredentialsException badCredentialsException) {

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(badCredentialsException.getMessage(), HttpStatus.BAD_REQUEST,"error");

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SecurityException.class)
    public final ResponseEntity<ApiErrorResponse> handleIncorrectPasswordException(SecurityException securityException) {

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(securityException.getMessage(), HttpStatus.BAD_REQUEST,"error");

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

}
