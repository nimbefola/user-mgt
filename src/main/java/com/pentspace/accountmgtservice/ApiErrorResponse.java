package com.pentspace.accountmgtservice;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ApiErrorResponse {

    private final String message;


}
