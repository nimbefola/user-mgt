package com.pentspace.usermgtservice.entities.repositories;
;
import com.pentspace.usermgtservice.entities.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankRepository extends JpaRepository<Bank, String> {

}
