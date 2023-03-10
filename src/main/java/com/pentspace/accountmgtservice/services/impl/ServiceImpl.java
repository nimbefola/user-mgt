package com.pentspace.accountmgtservice.services.impl;

import com.pentspace.accountmgtservice.entities.repositories.ServiceRepository;
import com.pentspace.accountmgtservice.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServiceImpl implements ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;
    @Override
    public com.pentspace.accountmgtservice.entities.Service createService(com.pentspace.accountmgtservice.entities.Service service) {
        return serviceRepository.save(service);
    }

    @Override
    public com.pentspace.accountmgtservice.entities.Service getByID(String id) {
        return serviceRepository.findById(id).orElseThrow(()->new NoSuchElementException("Service not found"));
    }

    @Override
    public List<com.pentspace.accountmgtservice.entities.Service> getAll() {
        return serviceRepository.findAll();
    }

}
