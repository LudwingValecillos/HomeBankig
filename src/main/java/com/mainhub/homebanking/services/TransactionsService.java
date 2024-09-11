package com.mainhub.homebanking.services;

import com.mainhub.homebanking.DTO.NewTransactionDTO;
import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Client;
import org.springframework.http.ResponseEntity;

public interface TransactionsService {

    ResponseEntity<?> processTransaction(String email, NewTransactionDTO transactionDTO);

    String validateTransaction(NewTransactionDTO transactionDTO);

    String checkClientAndAccounts(String email, NewTransactionDTO transactionDTO);

    String validateAmount(NewTransactionDTO transactionDTO);

    String performTransaction(NewTransactionDTO transactionDTO);

    Account getSourceAccount(NewTransactionDTO transactionDTO);

    Account getDestinationAccount(NewTransactionDTO transactionDTO);

    Client getClient(String email);
}
