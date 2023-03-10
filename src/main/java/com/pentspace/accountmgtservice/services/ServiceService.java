package com.pentspace.accountmgtservice.services;

import com.pentspace.accountmgtservice.entities.Service;

import java.util.List;

public interface ServiceService {
    Service createService(Service service);
    Service getByID(String  id);
    List<Service> getAll();

}
