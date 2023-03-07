package com.pentspace.usermgtservice.entities.repositories;

import com.pentspace.usermgtservice.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByUsernameAndPassword(String username, String password);
     Optional<Account>  findByMsisdn(String msisdn);
    Optional<Account> findByEmail(String email);
    Optional<Account> findByEmailAndPin(String email, String pin);
    Optional<Account> findByUsername(String username);

}
