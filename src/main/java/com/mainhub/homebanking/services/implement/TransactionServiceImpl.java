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
    public ResponseEntity<String> processTransaction(String email, NewTransactionDTO transactionDTO) {

        // Validate transaction data
        ResponseEntity<String> validationResponse = validateTransaction(transactionDTO);
        if (validationResponse != null) {
            return validationResponse;
        }

        // Check client and accounts
        ResponseEntity<String> clientAccountResponse = checkClientAndAccounts(email, transactionDTO);
        if (clientAccountResponse != null) {
            return clientAccountResponse;
        }

        // Perform transaction
        return performTransaction(transactionDTO);
    }

    public ResponseEntity<String> validateTransaction(NewTransactionDTO transactionDTO) {
        if (transactionDTO.amount() == 0 || transactionDTO.description().isBlank()
                || transactionDTO.sourceAccount().isBlank() || transactionDTO.destinationAccount().isBlank()) {
            return new ResponseEntity<>("All fields are required", HttpStatus.BAD_REQUEST);
        }
        if (transactionDTO.sourceAccount().equals(transactionDTO.destinationAccount())) {
            return new ResponseEntity<>("Source and destination accounts cannot be the same", HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    public ResponseEntity<String> checkClientAndAccounts(String email, NewTransactionDTO transactionDTO) {
        Client client = clientRepository.findByEmail(email);

        if (client == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.BAD_REQUEST);
        }

        if (client.getAccounts().stream().noneMatch(account -> account.getNumber().equals(transactionDTO.sourceAccount()))) {
            return new ResponseEntity<>("Source account not found", HttpStatus.BAD_REQUEST);
        }

        if (!accountRepository.existsByNumber(transactionDTO.destinationAccount())) {
            return new ResponseEntity<>("Destination account not found", HttpStatus.BAD_REQUEST);
        }

        return null;
    }

    public ResponseEntity<String> performTransaction(NewTransactionDTO transactionDTO) {
        Account sourceAccount = accountRepository.findByNumber(transactionDTO.sourceAccount());
        Account destinationAccount = accountRepository.findByNumber(transactionDTO.destinationAccount());

        if (sourceAccount.getBalance() < transactionDTO.amount()) {
            return new ResponseEntity<>("Not enough balance", HttpStatus.BAD_REQUEST);
        }

        Transaction transactionSource = new Transaction(TransactionType.DEBIT, -transactionDTO.amount(), transactionDTO.description());
        sourceAccount.addTransaction(transactionSource);

        Transaction transactionDestination = new Transaction(TransactionType.CREDIT, transactionDTO.amount(), transactionDTO.description());
        destinationAccount.addTransaction(transactionDestination);

        transactionRepository.save(transactionSource);
        transactionRepository.save(transactionDestination);

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        return new ResponseEntity<>("Transaction created", HttpStatus.CREATED);
    }
}
