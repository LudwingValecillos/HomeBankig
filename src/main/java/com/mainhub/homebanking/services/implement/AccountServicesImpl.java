package com.mainhub.homebanking.services.implement;

import com.mainhub.homebanking.DTO.AccountDTO;
import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.repositories.AccountRepository;
import com.mainhub.homebanking.repositories.ClientRepository;
import com.mainhub.homebanking.services.AccountServices;
import com.mainhub.homebanking.utils.GenerateNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class AccountServicesImpl implements AccountServices {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private GenerateNumber num;

    private List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
    @Override
    public List<AccountDTO> getAllAccountsDTO() {
        return getAllAccounts().stream().map(AccountDTO::new).collect(toList());
    }

    private Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public ResponseEntity<AccountDTO> getAccountDTOById(Long id) {
        return ResponseEntity.ok(new AccountDTO(getAccountById(id)));
    }

    @Override
    public ResponseEntity<?> createAccount(Authentication authentication) {

        Client client = clientRepository.findByEmail(authentication.getName());

        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("You can't have more than 3 accounts", HttpStatus.FORBIDDEN);
        }

        // Crear y configurar la nueva cuenta
        Account account = new Account(num.generateAccountNumber(), LocalDate.now(), 0);

        client.addAccount(account);

        // Guardar la cuenta en la base de datos
        accountRepository.save(account);

        // Agregar la nueva cuenta al cliente
        clientRepository.save(client);

        return new ResponseEntity<>("Account created", HttpStatus.CREATED);
    }
}
