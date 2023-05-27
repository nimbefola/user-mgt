package com.pentspace.accountmgtservice.exceptions;

import com.pentspace.accountmgtservice.ApiErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RestControllerAdvice
public class CustomizedResponseEntityHandler extends ResponseEntityExceptionHandler {

        @ExceptionHandler(Exception.class)
        public final ResponseEntity<ApiErrorResponse> handleAllExceptions(Exception exception) {
            ApiErrorResponse apiErrorResponse = new ApiErrorResponse(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

            return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    @ExceptionHandler(MessagingException.class)
    public final ResponseEntity<ApiErrorResponse> handleAllExceptions(MessagingException messagingException) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(messagingException.getMessage(),HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

        @ExceptionHandler(AuthorizationException.class)
        public final ResponseEntity<ApiErrorResponse> handleUserNotFoundException(AuthorizationException authorizationException) {
            ApiErrorResponse apiErrorResponse = new ApiErrorResponse(authorizationException.getMessage(),HttpStatus.UNAUTHORIZED);

            return new ResponseEntity<>(apiErrorResponse, HttpStatus.UNAUTHORIZED);
        }

    @ExceptionHandler(GeneralServiceException.class)
    public final ResponseEntity<ApiErrorResponse> handleUserNotFoundException(GeneralServiceException generalServiceException,
                                                                              WebRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(generalServiceException.getMessage(),HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(AccountCreationException.class)
    public final ResponseEntity<ApiErrorResponse> handleUserNotFoundException(AccountCreationException accountCreationException) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(accountCreationException.getMessage(),HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(UsernameNotFoundException.class)
    public final ResponseEntity<ApiErrorResponse> handleUserNotFoundException(UsernameNotFoundException usernameNotFoundException
                                                                              ) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(usernameNotFoundException.getMessage(),HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public final ResponseEntity<ApiErrorResponse> handleUserNotFoundException(IncorrectPasswordException incorrectPasswordException) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(incorrectPasswordException.getMessage(),HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> details = new ArrayList<>();
        for(ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        ApiErrorResponse error = new ApiErrorResponse("Validation Failed",HttpStatus.BAD_REQUEST );
        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
    }

}
