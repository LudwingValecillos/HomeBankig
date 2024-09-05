package com.mainhub.homebanking.controllers;


import com.mainhub.homebanking.DTO.NewTransactionDTO;
import com.mainhub.homebanking.repositories.AccountRepository;
import com.mainhub.homebanking.repositories.ClientRepository;
import com.mainhub.homebanking.repositories.TransactionRepository;
import com.mainhub.homebanking.services.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TransactionsService transactionService;



    @PostMapping("/clients/current/transactions")
    public ResponseEntity<?> createTransaction(Authentication authentication, @RequestBody NewTransactionDTO transaction) {

        return transactionService.processTransaction(authentication.getName(), transaction);
    }

}
