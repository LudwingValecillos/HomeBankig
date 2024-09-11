package com.mainhub.homebanking.services.implement;

import com.mainhub.homebanking.DTO.ClientDTO;
import com.mainhub.homebanking.DTO.LoanDTO;
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

            if (validateLogin(loginDTO) != null) {
                return new ResponseEntity<>(validateLogin(loginDTO), HttpStatus.BAD_REQUEST);
            }

            authenticate(loginDTO);
            return ResponseEntity.ok(generateToken(getUserDetailsService(loginDTO)));

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Email or password invalid", HttpStatus.BAD_REQUEST);
        }
    }

    public String validateLogin(LoginDTO loginDTO) {

        if (loginDTO.email().isEmpty() || loginDTO.password().isEmpty()) {
            return "Email or password invalid";
        }
        return null;
    }

    public void authenticate(LoginDTO loginDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password()));
    }

    public String generateToken(UserDetails userDetails) {

        return jwtUtilService.generateToken(userDetails);
    }

    public UserDetails getUserDetailsService(LoginDTO loginDTO) {

        return userDetailsService.loadUserByUsername(loginDTO.email());
    }


    @Override
    public ResponseEntity<?> register(RegisterDTO registerDTO) {

        ResponseEntity<?> validationResponse = validateRegister(registerDTO);
        if (validationResponse != null) {
            return validationResponse;
        }

        saveClientAndAccount(generateClient(registerDTO), generateAccount(registerDTO));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<?> validateRegister(RegisterDTO registerDTO) {
        // Verifico si el correo ya existe
        if (clientRepository.findByEmail(registerDTO.email()) != null) {
            return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
        }

        if (registerDTO.firstName().isBlank()) {
            return new ResponseEntity<>("First name is required", HttpStatus.BAD_REQUEST);
        }

        if (registerDTO.lastName().isBlank()) {
            return new ResponseEntity<>("Last name is required", HttpStatus.BAD_REQUEST);
        }

        if (registerDTO.email().isBlank()) {
            return new ResponseEntity<>("Email is required", HttpStatus.BAD_REQUEST);
        }

        if (registerDTO.password().isBlank()) {
            return new ResponseEntity<>("Password is required", HttpStatus.BAD_REQUEST);
        }

        if (registerDTO.password().length() < 8) {
            return new ResponseEntity<>("Password must be at least 8 characters long", HttpStatus.BAD_REQUEST);
        }

        if (!registerDTO.password().matches(".*[A-Z].*")) {
            return new ResponseEntity<>("Password must contain at least one uppercase letter", HttpStatus.BAD_REQUEST);
        }
        if (!registerDTO.password().matches(".*[a-z].*")) {
            return new ResponseEntity<>("Password must contain at least one lowercase letter", HttpStatus.BAD_REQUEST);
        }
        if (!registerDTO.password().matches(".*[0-9].*")) {
            return new ResponseEntity<>("Password must contain at least one number", HttpStatus.BAD_REQUEST);
        }
        if (!registerDTO.password().matches(".*[^a-zA-Z0-9].*")) {
            return new ResponseEntity<>("Password must contain at least one symbol", HttpStatus.BAD_REQUEST);
        }

        return null;
    }


    public Account generateAccount(RegisterDTO registerDTO) {
        return new Account(num.generateAccountNumber(), LocalDate.now(), 0);
    }

    public Client generateClient(RegisterDTO registerDTO) {
        return new Client(registerDTO.firstName(), registerDTO.lastName(), registerDTO.email(), passwordEncoder.encode(registerDTO.password()));
    }

    public void saveClientAndAccount(Client client, Account account) {
        client.addAccount(account);
        clientRepository.save(client);
        accountRepository.save(account);
    }

    @Override
    public ResponseEntity<ClientDTO> getClient(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        return ResponseEntity.ok(new ClientDTO(client));
    }
}
