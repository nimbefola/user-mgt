package com.pentspace.usermgtservice.endpoints;

import com.pentspace.usermgtservice.dto.AccountDTO;
import com.pentspace.usermgtservice.dto.ServiceDTO;
import com.pentspace.usermgtservice.entities.Account;
import com.pentspace.usermgtservice.entities.Service;
import com.pentspace.usermgtservice.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "service")
public class ServiceEndpoint {
    @Autowired
    private ServiceService serviceService;

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<Service> create(@RequestBody @Valid ServiceDTO serviceDTO) {
        Service service = Service.build(serviceDTO.getTitle(), serviceDTO.getDescription(), serviceDTO.getServiceImageUrl(), null);
        return new ResponseEntity<>(serviceService.createService(service), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Service> getById(@PathVariable("id") String accountId){
        return new ResponseEntity<>(serviceService.getByID(accountId), HttpStatus.OK);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Service>> getAll(){
        return new ResponseEntity<>(serviceService.getAll(), HttpStatus.OK);
    }
}
