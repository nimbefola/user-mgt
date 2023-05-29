package com.pentspace.accountmgtservice.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

@Data
@RequiredArgsConstructor
public class ApiErrorResponse {

    private String message;

    private HttpStatus status;
    private List<String> errors;

    public ApiErrorResponse(String message, HttpStatus status, List<String> errors) {
        super();
        this.message = message;
        this.status = status;
        this.errors = errors;
    }

    public ApiErrorResponse(String message, HttpStatus status, String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }

    public ApiErrorResponse(String message, HttpStatus status) {
        super();
        this.status = status;
        this.message = message;
    }

}
