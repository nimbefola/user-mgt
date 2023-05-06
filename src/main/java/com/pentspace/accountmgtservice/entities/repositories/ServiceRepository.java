package com.pentspace.accountmgtservice.entities.repositories;

import com.pentspace.accountmgtservice.entities.Account;
import com.pentspace.accountmgtservice.entities.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, String> {

    Optional<Service> findByTitle(String title);
}
