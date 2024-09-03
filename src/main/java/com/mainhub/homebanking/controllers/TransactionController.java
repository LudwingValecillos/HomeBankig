package com.mainhub.homebanking.controllers;


import com.mainhub.homebanking.DTO.TransactionDTO;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;



    @PostMapping("/clients/current/transactions")
    public ResponseEntity<?> createTransaction(Authentication authentication, @RequestBody TransactionDTO transactionDTO) {

        if(transactionDTO.type() == null || transactionDTO.amount() == 0 || transactionDTO.description().isBlank()
                || transactionDTO.SourceAccount().isBlank() || transactionDTO.DestinationAccount().isBlank()) {

            return new ResponseEntity<>("All fields are required", HttpStatus.BAD_REQUEST);
        }

        Client client = clientRepository.findByEmail(authentication.getName());


        if(client == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }

        // Verifica si el cliente es propietario de la cuenta de origen
        if(client.getAccounts().contains(transactionDTO.SourceAccount())) {
            return new ResponseEntity<>("Source account not found", HttpStatus.NOT_FOUND);
        }

        // Verifica la cuenta de destino
        if(!accountRepository.existsByNumber(transactionDTO.DestinationAccount())) {
            return new ResponseEntity<>("Destination account not found", HttpStatus.NOT_FOUND);
        }


        Account sourceAccount = accountRepository.findByNumber(transactionDTO.SourceAccount());
        Account destinationAccount = accountRepository.findByNumber(transactionDTO.DestinationAccount());

        Transaction transactionSource = new Transaction( TransactionType.DEBIT, transactionDTO.amount(), transactionDTO.description());
        sourceAccount.addTransaction(transactionSource);

        Transaction transactionDestination = new Transaction(TransactionType.CREDIT, transactionDTO.amount(), transactionDTO.description());
        destinationAccount.addTransaction(transactionDestination);


        transactionRepository.save(transactionSource);
        transactionRepository.save(transactionDestination);

        return new ResponseEntity<>("Transaction created", HttpStatus.CREATED);
    }

}
