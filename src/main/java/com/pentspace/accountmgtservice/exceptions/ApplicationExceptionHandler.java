package com.pentspace.accountmgtservice.exceptions;

import feign.FeignException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

//@RestControllerAdvice
//public class ApplicationExceptionHandler {
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public Map<String, String> handleException(MethodArgumentNotValidException exception){
//        Map<String, String> errorMap = new HashMap<>();
//        exception.getBindingResult().getFieldErrors().forEach(error ->{
//            errorMap.put(error.getField(), error.getDefaultMessage());
//        } );
//        return errorMap;
//    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler({DataIntegrityViolationException.class})
//    public Map<String, String> handleDuplicateConstrain(DataIntegrityViolationException exception){
//        Map<String, String> errorMap = new HashMap<>();
//        errorMap.put("error", exception.getMessage());
//        return errorMap;
//    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(NoSuchElementException.class)
//    public Map<String, String> handleDuplicateConstrain(NoSuchElementException exception){
//        Map<String, String> errorMap = new HashMap<>();
//        errorMap.put("error", exception.getMessage());
//        return errorMap;
//    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(GeneralServiceException.class)
//    public Map<String, String> handleDuplicateConstrain(GeneralServiceException exception){
//        Map<String, String> errorMap = new HashMap<>();
//        errorMap.put("error", exception.getMessage());
//        return errorMap;
//    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(RuntimeException.class)
//    public Map<String, String> handleDuplicateConstrain(RuntimeException exception){
//        Map<String, String> errorMap = new HashMap<>();
//        errorMap.put("error", exception.getMessage());
//        return errorMap;
//    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(FeignException.class)
//    public Map<String, String> handleDuplicateConstrain(FeignException exception){
//        Map<String, String> errorMap = new HashMap<>();
//        errorMap.put("error", exception.getMessage());
//        return errorMap;
//    }
//}
