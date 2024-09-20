package com.mainhub.homebanking.services;

import com.mainhub.homebanking.DTO.ClientDTO;
import com.mainhub.homebanking.DTO.LoginDTO;
import com.mainhub.homebanking.DTO.RegisterDTO;
import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AuthService {

    ResponseEntity<?> login(LoginDTO loginDTO);

    ResponseEntity<?> register(RegisterDTO registerDTO);

    ResponseEntity<ClientDTO> getClient(Authentication authentication);

    ResponseEntity<?> validateRegister(RegisterDTO registerDTO);

    void saveClientAndAccount(Client client, Account account);

    Account generateAccount(RegisterDTO registerDTO);

    Client generateClient(RegisterDTO registerDTO);
}
