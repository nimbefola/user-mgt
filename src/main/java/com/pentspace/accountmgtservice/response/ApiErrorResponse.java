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

    private int status;


    public ApiErrorResponse(String message, int status) {
        super();
        this.status = status;
        this.message = message;
    }

}
