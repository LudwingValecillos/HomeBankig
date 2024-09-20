package com.mainhub.homebanking.services;

import com.mainhub.homebanking.DTO.LoanAplicationDTO;
import com.mainhub.homebanking.DTO.LoanDTO;
import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.models.ClientLoan;
import com.mainhub.homebanking.models.Loan;
import com.mainhub.homebanking.models.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface LoanServices {

    List<LoanDTO> getAllLoansDTO();

    ResponseEntity<?> applyForLoan(Authentication authentication, LoanAplicationDTO loanDTO);

    ResponseEntity<?> loansAvailable(Authentication authentication);

    ResponseEntity<?> loansApplied(Authentication authentication);

    Loan getLoan(Long id);

    Account getAccount(String accountNumber);

    Client getClient(String email);

    String processLoanApplication(Client client, LoanAplicationDTO loanAplicationDTO, Account destinationAccount);

    Transaction generateTransactionLoan(Loan loan, LoanAplicationDTO loanAplicationDTO);

    ClientLoan generateClientLoan(Loan loan, LoanAplicationDTO loanAplicationDTO);

    double calculateLoanAmount(LoanAplicationDTO loanDTO);

    boolean clientHasAppliedForLoan(Client client, Loan loan);

    String validateLoanApplication(LoanAplicationDTO loanDTO, Client client, Loan loan);
}
