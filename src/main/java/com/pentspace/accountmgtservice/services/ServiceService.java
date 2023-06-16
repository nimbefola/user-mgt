package com.pentspace.accountmgtservice.services;

import com.pentspace.accountmgtservice.dto.ServiceDTO;
import com.pentspace.accountmgtservice.entities.Service;
import com.pentspace.accountmgtservice.entities.User;
import com.pentspace.accountmgtservice.exceptions.AuthorizationException;
import com.pentspace.accountmgtservice.exceptions.GeneralServiceException;

import java.util.List;

public interface ServiceService {
    Service createService(ServiceDTO serviceDTO,String id) throws GeneralServiceException;
    Service getByID(String  id);
    List<Service> getAll();

}
