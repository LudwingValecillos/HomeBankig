package com.mainhub.homebanking.services;

import com.mainhub.homebanking.DTO.LoanAplicationDTO;
import com.mainhub.homebanking.DTO.LoanDTO;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.models.Loan;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

public interface LoanServices {
//    List<Loan> getAllLoans();

    List<LoanDTO> getAllLoansDTO();

    ResponseEntity<?> applyForLoan(Authentication authentication, LoanAplicationDTO loanDTO);

    ResponseEntity<?> getLoansClient(Authentication authentication);
}
