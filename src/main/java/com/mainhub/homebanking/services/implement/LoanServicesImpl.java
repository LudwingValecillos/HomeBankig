package com.mainhub.homebanking.services.implement;

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

//    @Override
//    public List<Loan> getAllLoans() {
//        return null;
//    }

    @Override
    public List<LoanDTO> getAllLoansDTO() {
        return loanRepository.findAll().stream()
                .map(LoanDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ResponseEntity<String> applyForLoan(Authentication authentication, LoanAplicationDTO loanDTO) {
        Client client = clientRepository.findByEmail(authentication.getName());

        // Validaciones previas
        ResponseEntity<String> validationResponse = validateLoanApplication(loanDTO, client);
        if (validationResponse != null) {
            return validationResponse;
        }

        Loan loan = loanRepository.findById(loanDTO.id()).orElse(null);
        validationResponse = validateLoanDetails(loanDTO, loan);
        if (validationResponse != null) {
            return validationResponse;
        }

        //Validacion si el cliente ya aplico para el prestamo
        if (clientHasAppliedForLoan(client, loan)) {
            return new ResponseEntity<>("Loan already applied", HttpStatus.BAD_REQUEST);
        }

        Account account = accountRepository.findByNumber(loanDTO.destinationAccount());
        if (account == null) {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }

        LoanApplication(client, loan, loanDTO, account);

        return new ResponseEntity<>("Loan " + loan.getName() + " applied successfully", HttpStatus.OK);
    }

    private boolean clientHasAppliedForLoan(Client client, Loan loan) {
        return client.getLoans().stream()
                .anyMatch(c -> c.getLoan().getId() == loan.getId());
    }

    private void LoanApplication(Client client, Loan loan, LoanAplicationDTO loanDTO, Account account) {
        // Crear y registrar transacción
        Transaction transaction = new Transaction(TransactionType.CREDIT, loanDTO.amount(), "Loan " + loan.getName());
        account.addTransaction(transaction);

        // Crear y asociar ClientLoan
        ClientLoan clientLoan = new ClientLoan(loanDTO.amount(), loanDTO.payments());
        client.addClientLoan(clientLoan);
        loan.addClientLoan(clientLoan);

        // Guardar cambios
        accountRepository.save(account);
        transactionRepository.save(transaction);
        clientLoanRepository.save(clientLoan);
        clientRepository.save(client);
    }

    private ResponseEntity<String> validateLoanApplication(LoanAplicationDTO loanDTO, Client client) {
        if (loanDTO.amount() <= 0 || loanDTO.payments() <= 0 || loanDTO.destinationAccount().isBlank() || loanDTO.id() <= 0) {
            return new ResponseEntity<>("The amount or description is empty", HttpStatus.BAD_REQUEST);
        }
        if (!validations.validateAccountToClient(loanDTO.destinationAccount(), client)) {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }
        return null;
    }

    private ResponseEntity<String> validateLoanDetails(LoanAplicationDTO loanDTO, Loan loan) {
        if (loan == null) {
            return new ResponseEntity<>("Loan not found", HttpStatus.NOT_FOUND);
        }
        if (loanDTO.amount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("Amount invalid", HttpStatus.BAD_REQUEST);
        }
        if (!loan.getPayments().contains(loanDTO.payments())) {
            return new ResponseEntity<>("Payments invalid", HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @Override
    public ResponseEntity<?> getLoansClient(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());

        Set<Long> clientLoanIds = client.getLoans().stream()
                .map(clientLoan -> clientLoan.getLoan().getId())
                .collect(Collectors.toSet());

        List<LoanDTO> availableLoans = loanRepository.findAll().stream()
                .filter(loan -> !clientLoanIds.contains(loan.getId()))
                .map(LoanDTO::new)
                .collect(Collectors.toList());

        if (availableLoans.isEmpty()) {
            return new ResponseEntity<>("No loans available", HttpStatus.OK);
        }

        return new ResponseEntity<>(availableLoans, HttpStatus.OK);
    }
}
