package com.pentspace.accountmgtservice.entities.repositories;

import com.pentspace.accountmgtservice.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

}
