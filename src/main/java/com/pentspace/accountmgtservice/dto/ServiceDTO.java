package com.pentspace.accountmgtservice.dto;

import com.pentspace.accountmgtservice.entities.Account;
import lombok.Data;

import java.util.Set;


public class ServiceDTO {
    private String title;
    private String description;
    private String serviceImageUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServiceImageUrl() {
        return serviceImageUrl;
    }

    public void setServiceImageUrl(String serviceImageUrl) {
        this.serviceImageUrl = serviceImageUrl;
    }

    private Set<Account> accounts;
}
