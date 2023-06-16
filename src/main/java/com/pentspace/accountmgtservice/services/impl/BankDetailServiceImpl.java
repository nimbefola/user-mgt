package com.pentspace.accountmgtservice.services.impl;

import com.pentspace.accountmgtservice.dto.BankDetailsDTO;
import com.pentspace.accountmgtservice.entities.BankDetail;
import com.pentspace.accountmgtservice.entities.repositories.BankDetailRepository;
import com.pentspace.accountmgtservice.services.BankDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankDetailServiceImpl implements BankDetailService {

    @Autowired
   private BankDetailRepository bankDetailRepository;

    @Override
    public BankDetail addBankDetail(BankDetailsDTO bankDetailDto) {

        BankDetail bankDetails = new BankDetail();
        bankDetails.setBankCode(bankDetailDto.getBankCode());
        bankDetails.setCbnBankCode(bankDetailDto.getCbnBankCode());
        bankDetails.setBankName(bankDetailDto.getBankName());
        bankDetails.setAccountNumber(bankDetailDto.getAccountNumber());
        bankDetails.setAccountName(bankDetailDto.getAccountName());

      return bankDetailRepository.save(bankDetails);
    }
}
