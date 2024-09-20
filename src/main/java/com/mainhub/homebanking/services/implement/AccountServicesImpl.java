package com.mainhub.homebanking.services.implement;

import com.mainhub.homebanking.DTO.AccountDTO;
import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.repositories.AccountRepository;
import com.mainhub.homebanking.repositories.ClientRepository;
import com.mainhub.homebanking.services.AccountServices;
import com.mainhub.homebanking.utils.GenerateNumber;
import com.mainhub.homebanking.utils.Validations;
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

    @Autowired
    private Validations validations;

    @Override
    public List<AccountDTO> getAllAccountsDTO() {
        return accountRepository.findAll().stream()
                .map(AccountDTO::new)
                .collect(toList());
    }

    @Override
    public ResponseEntity<AccountDTO> getAccountDTOById(Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        return ResponseEntity.ok(new AccountDTO(account));
    }

    @Override
    public ResponseEntity<?> createAccount(Authentication authentication) {

        if (accountRepository.findByClient(getClient(authentication)).size() >= 3) {
            return new ResponseEntity<>("You can't have more than 3 accounts", HttpStatus.FORBIDDEN);
        }


        return new ResponseEntity<>(saveAccount(getClient(authentication), createAccount())
                , HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> deleteAccount(Long id, Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findById(id).orElse(null);

        if (account == null || !validations.validateAccountToClient(account.getNumber(), client)) {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }

        if (account.getBalance() != 0) {
            return new ResponseEntity<>("Account has balance, please transfer it first", HttpStatus.BAD_REQUEST);
        }

        accountRepository.delete(account);
        return new ResponseEntity<>("Account deleted", HttpStatus.OK);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public List<Account> getAccountsByClient(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        return accountRepository.findByClient(client);
    }
    @Override
    public AccountDTO saveAccount(Client client, Account account) {
        client.addAccount(account);
        accountRepository.save(account);
        clientRepository.save(client);
        return new AccountDTO(account);
    }
    @Override
    public Account createAccount() {
        return new Account(num.generateAccountNumber(), LocalDate.now(), 0);
    }

    @Override
    public Client getClient(Authentication authentication) {
        return clientRepository.findByEmail(authentication.getName());
    }
}
