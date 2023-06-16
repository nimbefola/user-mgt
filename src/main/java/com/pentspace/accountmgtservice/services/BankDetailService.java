package com.pentspace.accountmgtservice.services;

import com.pentspace.accountmgtservice.dto.BankDetailsDTO;
import com.pentspace.accountmgtservice.entities.BankDetail;

public interface BankDetailService {

    BankDetail addBankDetail(BankDetailsDTO bankDetailDto);
}
