package com.pentspace.accountmgtservice;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@RequiredArgsConstructor
public class ApiErrorResponse {

    private final String message;

    private final HttpStatus status;


}
