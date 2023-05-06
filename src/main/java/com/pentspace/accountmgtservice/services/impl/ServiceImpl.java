package com.pentspace.accountmgtservice.services.impl;

import com.pentspace.accountmgtservice.entities.Account;
import com.pentspace.accountmgtservice.entities.repositories.ServiceRepository;
import com.pentspace.accountmgtservice.exceptions.AuthorizationException;
import com.pentspace.accountmgtservice.exceptions.GeneralServiceException;
import com.pentspace.accountmgtservice.security.securityServices.UserPrincipalService;
import com.pentspace.accountmgtservice.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ServiceImpl implements ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private UserPrincipalService userPrincipalService;

    @Override
    public com.pentspace.accountmgtservice.entities.Service createService(com.pentspace.accountmgtservice.entities.Service service,String authentication) throws AuthorizationException, GeneralServiceException {

        String userEmail = userPrincipalService.getUserEmailAddressFromToken(authentication);

        Optional<com.pentspace.accountmgtservice.entities.Service> user = serviceRepository.findByTitle(userEmail);
        if (!user.isPresent()) {
            throw new GeneralServiceException("User not found");
        }
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
