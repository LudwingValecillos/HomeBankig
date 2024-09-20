package com.mainhub.homebanking.services.implement;

import com.mainhub.homebanking.DTO.NewTransactionDTO;
import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.models.Transaction;
import com.mainhub.homebanking.models.type.TransactionType;
import com.mainhub.homebanking.repositories.AccountRepository;
import com.mainhub.homebanking.repositories.ClientRepository;
import com.mainhub.homebanking.repositories.TransactionRepository;
import com.mainhub.homebanking.services.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionServiceImpl implements TransactionsService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @Override
    public ResponseEntity<?> processTransaction(String email, NewTransactionDTO transactionDTO) {

        if (validateTransaction(transactionDTO) != null) {
            return new ResponseEntity<>(validateTransaction(transactionDTO), HttpStatus.BAD_REQUEST);
        }

        if (checkClientAndAccounts(email, transactionDTO) != null) {
            return new ResponseEntity<>(checkClientAndAccounts(email, transactionDTO), HttpStatus.BAD_REQUEST);
        }

        if (validateAmount(transactionDTO) != null) {
            return new ResponseEntity<>(validateAmount(transactionDTO), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(performTransaction(transactionDTO), HttpStatus.OK);
    }

    @Override
    public String validateTransaction(NewTransactionDTO transactionDTO) {
        if (transactionDTO.amount()  < 0) {
            return "The 'amount' field must be positive.";
        }
        if(transactionDTO.amount() == 0) {
            return "The 'amount' field is required and cannot be zero.";
        }

        if (transactionDTO.amount() == 0) {
            return "The 'amount' field is required and cannot be zero.";
        }

        if (transactionDTO.description().isBlank()) {
            return "The 'description' field is required.";
        }

        if (transactionDTO.sourceAccount().isBlank()) {
            return "The 'source account' field is required.";
        }

        if (transactionDTO.destinationAccount().isBlank()) {
            return "The 'destination account' field is required.";
        }

        if (transactionDTO.sourceAccount().equals(transactionDTO.destinationAccount())) {
            return "Source and destination accounts cannot be the same";
        }
        return null;
    }

    @Override
    public String checkClientAndAccounts(String email, NewTransactionDTO transactionDTO) {
        Client client = clientRepository.findByEmail(email);

        //Devuelve true si encuentra una cuenta con el número de cuenta
        if (!accountRepository.existsByNumber(transactionDTO.destinationAccount())) {
            return "Destination account not found";
        }
        //NoneMatch: devuelve true si ninguno de los elementos del stream coincide
        if (client.getAccounts().stream().noneMatch(account -> account.getNumber().equals(transactionDTO.sourceAccount()))) {
            return "Source account not found";
        }

        return null;
    }

    public Client getClient(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public String performTransaction(NewTransactionDTO transactionDTO) {
        getSourceAccount(transactionDTO).addTransaction(transactionRepository.save(new Transaction(TransactionType.DEBIT, -transactionDTO.amount(), transactionDTO.description())));
        getDestinationAccount(transactionDTO).addTransaction(transactionRepository.save(new Transaction(TransactionType.CREDIT, transactionDTO.amount(), transactionDTO.description())));
        return "Transaction created";
    }

    @Override
    public String validateAmount(NewTransactionDTO transactionDTO) {
        if (getSourceAccount(transactionDTO).getBalance() < transactionDTO.amount()) {
            return "Not enough balance";
        }
        return null;
    }

    @Override
    public Account getSourceAccount(NewTransactionDTO transactionDTO) {
        return accountRepository.findByNumber(transactionDTO.sourceAccount());
    }

    @Override
    public Account getDestinationAccount(NewTransactionDTO transactionDTO) {
        return accountRepository.findByNumber(transactionDTO.destinationAccount());
    }
}
