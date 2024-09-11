package com.mainhub.homebanking.controllers;

import com.mainhub.homebanking.DTO.AccountDTO;
import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.services.AccountServices;
import com.mainhub.homebanking.utils.GenerateNumber;
import com.mainhub.homebanking.repositories.AccountRepository;
import com.mainhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping("/api/accounts/")
public class AccountController {



    @Autowired
    private AccountServices accountServices;

    @GetMapping("/")
    // Maneja las solicitudes GET a la ruta base "/" para obtener todos los clientes.
    public List<AccountDTO> getAllClients() {
        return accountServices.getAllAccountsDTO();
    }

    @GetMapping("/id={id}")
    // Maneja las solicitudes GET para obtener un cliente por ID.
    public ResponseEntity<AccountDTO> getById(@PathVariable Long id) {
        return accountServices.getAccountDTOById(id);

    }

    @PostMapping("clients/current/accounts")
    public ResponseEntity<?> createAccount(Authentication authentication) {
        return accountServices.createAccount(authentication);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAccount(Authentication authentication,@RequestBody Long id ) {
        return accountServices.deleteAccount(id, authentication);
    }

    @GetMapping("/hello")
    public String getClients() {
        return "Hello Accounts, me gusta el pan!";
    }
}
