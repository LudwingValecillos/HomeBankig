package com.mainhub.homebanking.controllers;

import com.mainhub.homebanking.DTO.AccountDTO;
import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.models.utils.GenerateNumberCard;
import com.mainhub.homebanking.repositories.AccountRepository;
import com.mainhub.homebanking.repositories.ClientRepository;
import com.mainhub.homebanking.servicesSecurity.JwtUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AuthenticationManager authenticationManager; // Administrador de autenticación para manejar el proceso de autenticación.

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserDetailsService userDetailsService; // Servicio para cargar los detalles del usuario.

    GenerateNumberCard num = new GenerateNumberCard();

    @Autowired
    private JwtUtilService jwtUtilService; // Servicio para manejar la generación y validación de JWT (JSON Web Tokens).

    @GetMapping("/")
    // Maneja las solicitudes GET a la ruta base "/" para obtener todos los clientes.
    public List<AccountDTO> getAllClients() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());

    }

    @GetMapping("/id={id}")
    // Maneja las solicitudes GET para obtener un cliente por ID.
    public ResponseEntity<AccountDTO> getById(@PathVariable Long id) {
        return accountRepository.findById(id).map(AccountDTO::new) // Convertir Client a ClientDTO
                .map(ResponseEntity::ok) // Si está presente, devolver 200 OK con el ClientDTO
                .orElse(ResponseEntity.notFound().build());

    }
    @PostMapping("/current/new")
    public ResponseEntity<?> createAccount(Authentication authentication) {
        System.out.println("Creating account for: " + authentication.getName());
        Client client = clientRepository.findByEmail(authentication.getName());

        if (client.getAccounts().size() >2) {
            return ResponseEntity.status(403).body("You can't create more accounts");
        }

        Account account = new Account(num.accountNumber(), LocalDate.now(), 0);
        client.addAccount(account);
        accountRepository.save(account);

        return ResponseEntity.ok("Account created successfully");
    }


    @GetMapping("/hello")
    public String getClients() {
        return "Hello Accounts, me gusta el pan!";
    }
}
