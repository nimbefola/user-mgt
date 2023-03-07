package com.pentspace.usermgtservice.entities.repositories;

import com.pentspace.usermgtservice.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

}
