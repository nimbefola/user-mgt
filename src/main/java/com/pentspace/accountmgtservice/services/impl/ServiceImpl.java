package com.pentspace.accountmgtservice.services.impl;

import com.pentspace.accountmgtservice.dto.ServiceDTO;
import com.pentspace.accountmgtservice.entities.Account;
import com.pentspace.accountmgtservice.entities.Service;
import com.pentspace.accountmgtservice.entities.User;
import com.pentspace.accountmgtservice.entities.enums.AccountType;
import com.pentspace.accountmgtservice.entities.repositories.ServiceRepository;
import com.pentspace.accountmgtservice.entities.repositories.UserRepository;
import com.pentspace.accountmgtservice.exceptions.AuthorizationException;
import com.pentspace.accountmgtservice.exceptions.GeneralServiceException;
import com.pentspace.accountmgtservice.security.securityServices.UserPrincipalService;
import com.pentspace.accountmgtservice.serviceUtil.IdGenerator;
import com.pentspace.accountmgtservice.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;


import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceImpl implements ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Service createService(ServiceDTO serviceDTO,String id) throws GeneralServiceException {

       Optional <User> user = userRepository.findUserById(id);

       if (!user.isPresent()){
           throw new GeneralServiceException("NO USER RECORD FOUND");
       }

       User aUser = user.get();

       if (aUser.getAccountType().equals(AccountType.USER)){
           throw new GeneralServiceException("ONLY SERVICE PROVIDER CAN CREATE BUSINESS SERVICE");
       }

        Service service = new Service();
        service.setId(IdGenerator.generateId());
        service.setCreated(Date.from(Instant.now()));
        service.setUpdated(Date.from(Instant.now()));
        service.setTitle(serviceDTO.getTitle());
        service.setDescription(serviceDTO.getDescription());
        service.setServiceImageUrl(serviceDTO.getServiceImageUrl());
        service.setUser(aUser);


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
