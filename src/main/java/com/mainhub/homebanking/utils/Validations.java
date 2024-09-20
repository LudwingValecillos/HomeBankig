package com.mainhub.homebanking.utils;

import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Client;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class Validations {

    public boolean validateAccountToClient(String accountNumber, Client client) {

        return client.getAccounts().stream().noneMatch(account -> account.getNumber().equals(accountNumber));

    }
}
