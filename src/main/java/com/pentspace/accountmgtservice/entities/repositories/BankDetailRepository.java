package com.pentspace.accountmgtservice.entities.repositories;

import com.pentspace.accountmgtservice.entities.BankDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BankDetailRepository extends JpaRepository<BankDetail, String> {

}
