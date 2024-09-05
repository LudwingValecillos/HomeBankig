package com.mainhub.homebanking.services;

import com.mainhub.homebanking.DTO.NewTransactionDTO;
import org.springframework.http.ResponseEntity;

public interface TransactionsService {
    ResponseEntity<?> processTransaction(String email, NewTransactionDTO transactionDTO);

    ResponseEntity<?> validateTransaction(NewTransactionDTO transactionDTO);

    ResponseEntity<?> checkClientAndAccounts(String email, NewTransactionDTO transactionDTO);

    ResponseEntity<?> performTransaction(NewTransactionDTO transactionDTO);
}

