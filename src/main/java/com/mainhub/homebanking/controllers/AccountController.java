package com.mainhub.homebanking.controllers;

import com.mainhub.homebanking.DTO.AccountDTO;
import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.models.utils.GenerateNumberCard;
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
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private GenerateNumberCard num;

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

@PostMapping("clients/current/accounts")
public ResponseEntity<?> createAccount(Authentication authentication) {
    // Obtener el cliente autenticado
    Client client = clientRepository.findByEmail(authentication.getName());

    // Verifica si el cliente existe
    if (client == null) {
        return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
    }

    // Verifica si el cliente ya tiene tres cuentas
    if (client.getAccounts().size() >= 3) {
        return new ResponseEntity<>("You can't have more than 3 accounts", HttpStatus.FORBIDDEN);
    }

    // Crear y configurar la nueva cuenta
    Account account = new Account( num.generateAccountNumber(), LocalDate.now(), 0);

    client.addAccount(account);

    // Guardar la cuenta en la base de datos
    accountRepository.save(account);

    // Agregar la nueva cuenta al cliente
    clientRepository.save(client);


    return new ResponseEntity<>("Account created", HttpStatus.CREATED);
}

    @GetMapping("/hello")
    public String getClients() {
        return "Hello Accounts, me gusta el pan!";
    }
}
