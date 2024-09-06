package com.mainhub.homebanking.services;

import com.mainhub.homebanking.DTO.AccountDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AccountServices {

    List<AccountDTO> getAllAccountsDTO();

    ResponseEntity<AccountDTO> getAccountDTOById(Long id);

    ResponseEntity<?> createAccount(Authentication authentication);
}
