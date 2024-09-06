package com.mainhub.homebanking.services.impl;

import com.mainhub.homebanking.DTO.AccountDTO;
import com.mainhub.homebanking.DTO.ClientDTO;
import com.mainhub.homebanking.DTO.LoginDTO;
import com.mainhub.homebanking.DTO.RegisterDTO;
import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.repositories.AccountRepository;
import com.mainhub.homebanking.repositories.ClientRepository;
import com.mainhub.homebanking.services.AuthService;
import com.mainhub.homebanking.servicesSecurity.JwtUtilService;
import com.mainhub.homebanking.utils.GenerateNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @Autowired
    private GenerateNumber num;

    @Override
    public ResponseEntity<?> login(LoginDTO loginDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password()));

            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.email());
            final String jwt = jwtUtilService.generateToken(userDetails);

            return ResponseEntity.ok(jwt);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Email or password invalid", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> register(RegisterDTO registerDTO) {
        if (clientRepository.findByEmail(registerDTO.email()) != null) {
            return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
        }

        if (registerDTO.firstName().isBlank() || registerDTO.lastName().isBlank()) {
            return new ResponseEntity<>("First name and last name cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (registerDTO.password().length() < 8) {
            return new ResponseEntity<>("Password must be at least 8 characters long", HttpStatus.BAD_REQUEST);
        }

        Client newClient = new Client(registerDTO.firstName(), registerDTO.lastName(), registerDTO.email(), passwordEncoder.encode(registerDTO.password()));
        ClientDTO clientDTO = new ClientDTO(clientRepository.save(newClient));

        Account account = new Account(num.generateAccountNumber(), LocalDate.now(), 0);
        newClient.addAccount(account);
        AccountDTO accountDTO = new AccountDTO(accountRepository.save(account));

        clientRepository.save(newClient);

        return new ResponseEntity<>("Client registered successfully", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ClientDTO> getClient(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        return ResponseEntity.ok(new ClientDTO(client));
    }
}
