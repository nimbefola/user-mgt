package com.pentspace.accountmgtservice.entities.repositories;
;
import com.pentspace.accountmgtservice.entities.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Bank, String> {

}
