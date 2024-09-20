package com.mainhub.homebanking.services;

import com.mainhub.homebanking.DTO.AccountDTO;
import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AccountServices {

    List<AccountDTO> getAllAccountsDTO();

    ResponseEntity<AccountDTO> getAccountDTOById(Long id);

    ResponseEntity<?> createAccount(Authentication authentication);

    ResponseEntity<?> deleteAccount(Long id, Authentication authentication);

    List<Account> getAllAccounts();

    Account getAccountById(Long id);

    List<Account> getAccountsByClient(Authentication authentication);

    AccountDTO saveAccount(Client client, Account account);

    Account createAccount();

    Client getClient(Authentication authentication);
}
