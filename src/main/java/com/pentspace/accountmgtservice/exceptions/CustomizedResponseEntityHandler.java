package com.pentspace.accountmgtservice.exceptions;

import com.pentspace.accountmgtservice.ApiErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestControllerAdvice
public class CustomizedResponseEntityHandler  {

        @ExceptionHandler(Exception.class)
        public final ResponseEntity<ApiErrorResponse> handleAllExceptions(Exception exception) {
            ApiErrorResponse apiErrorResponse = new ApiErrorResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,"error");

            return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    @ExceptionHandler(MessagingException.class)
    public final ResponseEntity<ApiErrorResponse> handleMessagingExceptions(MessagingException messagingException) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(messagingException.getMessage(), HttpStatus.BAD_REQUEST,"error");

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

        @ExceptionHandler(AuthorizationException.class)
        public final ResponseEntity<ApiErrorResponse> handleAuthorizationException(AuthorizationException authorizationException) {
            ApiErrorResponse apiErrorResponse = new ApiErrorResponse(authorizationException.getMessage(),HttpStatus.UNAUTHORIZED,"error");

            return new ResponseEntity<>(apiErrorResponse, HttpStatus.UNAUTHORIZED);
        }

    @ExceptionHandler(GeneralServiceException.class)
    public final ResponseEntity<ApiErrorResponse> handleGeneralException(GeneralServiceException generalServiceException) {
       // System.out.println("Error messages "+ generalServiceException. );
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

//    @Override  // handleMethodArgumentNotValid
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        List<String> errorList = ex
//                .getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(fieldError -> fieldError.getDefaultMessage())
//                .collect(Collectors.toList());
//        ApiErrorResponse errorDetails = new ApiErrorResponse(ex.getMessage(),HttpStatus.BAD_REQUEST, errorList);
//        return handleExceptionInternal(ex, errorDetails, headers,HttpStatus.BAD_REQUEST, request);
//    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleException(MethodArgumentNotValidException exception){
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->{
            errorMap.put(error.getField(), error.getDefaultMessage());
        } );
        return errorMap;
    }

}
