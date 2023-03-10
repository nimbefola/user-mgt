package com.pentspace.usermgtservice.dto;

import lombok.Data;

import java.util.Map;

@Data
public class PaystackPaymentStatusDTO {
    private String status;
    private String message;
    private Map<String, String> data;
}
