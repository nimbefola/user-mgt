package com.pentspace.accountmgtservice;

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

        public ApiErrorResponse( String message,HttpStatus status, List<String> errors) {
            super();
            this.status = status;
            this.message = message;
            this.errors = errors;
        }

        public ApiErrorResponse( String message,HttpStatus status, String error) {
            super();
            this.status = status;
            this.message = message;
            errors = Arrays.asList(error);
        }

    public ApiErrorResponse( String message,HttpStatus status) {
        super();
        this.status = status;
        this.message = message;
    }
    }

//eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0aXRvNUBnbWFpbC5jb20iLCJyb2xlcyI6IkJBU0VfVVNFUiIsImlzcyI6InBlbnRzcGFjZSIsImlhdCI6MTY4NTIwMjQ2MSwiZXhwIjoxNjg1Mjg4ODYxfQ.F2Wi1v0nBBSoT9DCQovBGiPvOq6j4lEKJK_rDXXjbLm8LYglkMlK1RzR5jYoXcO5zDbeGGjcmC-svaB5oE5H6g

