package com.pentspace.usermgtservice.entities.repositories;

import com.pentspace.usermgtservice.entities.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<Service, String> {

}
