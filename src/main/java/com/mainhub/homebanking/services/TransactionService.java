package com.mainhub.homebanking.services;

import com.mainhub.homebanking.DTO.NewTransactionDTO;
import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.models.Transaction;
import com.mainhub.homebanking.models.type.TransactionType;
import com.mainhub.homebanking.repositories.AccountRepository;
import com.mainhub.homebanking.repositories.ClientRepository;
import com.mainhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public ResponseEntity<String> processTransaction(String email, NewTransactionDTO transaction) {

        if (transaction.amount() == 0 || transaction.description().isBlank()
                || transaction.sourceAccount().isBlank() || transaction.destinationAccount().isBlank()) {
            return new ResponseEntity<>("All fields are required", HttpStatus.BAD_REQUEST);
        }

        Client client = clientRepository.findByEmail(email);

        if (client == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.BAD_REQUEST);
        }

        // Verifica si el cliente es propietario de la cuenta de origen
        if (client.getAccounts().stream().noneMatch(account -> account.getNumber().equals(transaction.sourceAccount()))) {
            return new ResponseEntity<>("Source account not found", HttpStatus.BAD_REQUEST);
        }

        // Verifica la cuenta de destino
        if (!accountRepository.existsByNumber(transaction.destinationAccount())) {
            return new ResponseEntity<>("Destination account not found", HttpStatus.BAD_REQUEST);
        }

        if (transaction.sourceAccount().equals(transaction.destinationAccount())) {
            return new ResponseEntity<>("Source and destination accounts cannot be the same", HttpStatus.BAD_REQUEST);
        }

        Account sourceAccount = accountRepository.findByNumber(transaction.sourceAccount());
        Account destinationAccount = accountRepository.findByNumber(transaction.destinationAccount());

        if (sourceAccount.getBalance() < transaction.amount()) {
            return new ResponseEntity<>("Not enough balance", HttpStatus.BAD_REQUEST);
        }

        Transaction transactionSource = new Transaction(TransactionType.DEBIT, transaction.amount(), transaction.description());
        sourceAccount.addTransaction(transactionSource);

        Transaction transactionDestination = new Transaction(TransactionType.CREDIT, transaction.amount(), transaction.description());
        destinationAccount.addTransaction(transactionDestination);

        transactionRepository.save(transactionSource);
        transactionRepository.save(transactionDestination);

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        return new ResponseEntity<>("Transaction created", HttpStatus.CREATED);
    }
}
