package com.mainhub.homebanking.services;

import com.mainhub.homebanking.DTO.ClientDTO;
import com.mainhub.homebanking.DTO.LoginDTO;
import com.mainhub.homebanking.DTO.RegisterDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AuthService {

    ResponseEntity<?> login(LoginDTO loginDTO);

    ResponseEntity<?> register(RegisterDTO registerDTO);

    ResponseEntity<ClientDTO> getClient(Authentication authentication);
}
