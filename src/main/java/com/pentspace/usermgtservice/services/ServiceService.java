package com.pentspace.usermgtservice.services;

import com.pentspace.usermgtservice.entities.Service;

import java.util.List;

public interface ServiceService {
    Service createService(Service service);
    Service getByID(String  id);
    List<Service> getAll();

}
