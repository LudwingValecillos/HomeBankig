// LoanServicesImpl.java
package com.mainhub.homebanking.services.implement;

import com.mainhub.homebanking.DTO.ClientLoanDTO;
import com.mainhub.homebanking.DTO.LoanAplicationDTO;
import com.mainhub.homebanking.DTO.LoanDTO;
import com.mainhub.homebanking.models.*;
import com.mainhub.homebanking.models.type.TransactionType;
import com.mainhub.homebanking.repositories.*;
import com.mainhub.homebanking.services.LoanServices;
import com.mainhub.homebanking.utils.Validations;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LoanServicesImpl implements LoanServices {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private Validations validations;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<LoanDTO> getAllLoansDTO() {
        return loanRepository.findAll().stream()
                .map(LoanDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ResponseEntity<?> applyForLoan(Authentication authentication, LoanAplicationDTO loanAplicationDTO) {
        try {

            Client client = getClient(authentication.getName());

            Loan loan = getLoan(loanAplicationDTO.id());

            if (validateLoanApplication(loanAplicationDTO, client, loan) != null) {
                return new ResponseEntity<>(validateLoanApplication(loanAplicationDTO, client, loan), HttpStatus.BAD_REQUEST);
            }

            // Verifica si el cliente ya ha aplicado para este préstamo
            if (clientHasAppliedForLoan(client, loan)) {
                return new ResponseEntity<>("Loan already applied", HttpStatus.BAD_REQUEST);
            }

            // Procesa la solicitud de préstamo
            return new ResponseEntity<>(processLoanApplication(client, loanAplicationDTO, getAccount(loanAplicationDTO.destinationAccount())), HttpStatus.OK);
        } catch (Exception e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Client getClient(String email) {
        return clientRepository.findByEmail(email);
    }


    @Override
    public Loan getLoan(Long id) {
        return loanRepository.findById(id).orElse(null);
    }


    @Override
    public String validateLoanApplication(LoanAplicationDTO loanAplicationDTO, Client client, Loan loan) {

        if (loanAplicationDTO.amount() <= 0){
            return "Amount invalid";
        }
        if (loanAplicationDTO.payments() <= 0){
            return "Payments invalid";
        }
        if (loanAplicationDTO.destinationAccount().isBlank()){
            return "Account is blank";
        }
        if (loanAplicationDTO.id() <= 0){
            return "Id invalid";
        }

        if (loan == null) {
            return "Loan not found";
        }
        if (loanAplicationDTO.amount() > loan.getMaxAmount()) {
            return "Amount invalid";
        }
        if (!loan.getPayments().contains(loanAplicationDTO.payments())) {
            return "Payments invalid";
        }

        if (validations.validateAccountToClient(loanAplicationDTO.destinationAccount(), client)) {
            return "Account not found";
        }

        if (getAccount(loanAplicationDTO.destinationAccount()) == null) {
            return "Account not found";
        }


        return null;
    }


    @Override
    public Account getAccount(String accountNumber) {
        return accountRepository.findByNumber(accountNumber);
    }

    // Verifica si el cliente ya ha aplicado para este préstamo

    @Override
    public boolean clientHasAppliedForLoan(Client client, Loan loan) {
        return client.getLoans().stream()
                .anyMatch(c -> c.getLoan().getId() == loan.getId());
    }


    @Override
    public String processLoanApplication(Client client, LoanAplicationDTO loanAplicationDTO, Account destinationAccount) {

        destinationAccount.addTransaction(transactionRepository.save(generateTransactionLoan(getLoan(loanAplicationDTO.id()), loanAplicationDTO)));
        ClientLoan clientLoan = generateClientLoan(getLoan(loanAplicationDTO.id()), loanAplicationDTO);

        client.addClientLoan(clientLoan);
        getLoan(loanAplicationDTO.id()).addClientLoan(clientLoan);

        clientLoanRepository.save(clientLoan);

        return "Loan " + getLoan(loanAplicationDTO.id()).getName() + " applied successfully";
    }


    @Override
    public Transaction generateTransactionLoan(Loan loan, LoanAplicationDTO loanAplicationDTO) {
        return new Transaction(TransactionType.CREDIT, loanAplicationDTO.amount(), "Loan " + loan.getName());
    }


    @Override
    public ClientLoan generateClientLoan(Loan loan, LoanAplicationDTO loanAplicationDTO) {
        return new ClientLoan(calculateLoanAmount(loanAplicationDTO), loanAplicationDTO.payments());
    }


    @Override
    public double calculateLoanAmount(LoanAplicationDTO loanDTO) {
        double amount = loanDTO.amount();
        double rate = loanDTO.payments() <= 12 ? 0.15 : loanDTO.payments() > 12 ? 0.25 : 0.20;
        return (amount * rate) + amount;
    }

    @Override
    public ResponseEntity<?> loansAvailable(Authentication authentication) {
        List<LoanDTO> availableLoans = getAvailableLoansForClient(getClient(authentication.getName()));
        if (availableLoans.isEmpty()) {
            return new ResponseEntity<>("No loans available", HttpStatus.OK);
        }

        return new ResponseEntity<>(availableLoans, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> loansApplied(Authentication authentication) {
        Client client = getClient(authentication.getName());
        List<ClientLoanDTO> appliedLoans = client.getLoans().stream()
                .map(ClientLoanDTO::new)
                .collect(Collectors.toList());

        return new ResponseEntity<>(appliedLoans, HttpStatus.OK);
    }

    // getAvailableLoansForClient obtiene los préstamos disponibles para el cliente

    public List<LoanDTO> getAvailableLoansForClient(Client client) {
        Set<Long> clientLoanIds = client.getLoans().stream()
                .map(clientLoan -> clientLoan.getLoan().getId())
                .collect(Collectors.toSet());

        return loanRepository.findAll().stream()
                .filter(loan -> !clientLoanIds.contains(loan.getId()))
                .map(LoanDTO::new)
                .collect(Collectors.toList());
    }

}
