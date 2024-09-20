//package com.mainhub.homebanking.ServicesTest;
//
//import com.mainhub.homebanking.models.Account;
//import com.mainhub.homebanking.models.Client;
//import com.mainhub.homebanking.repositories.AccountRepository;
//import com.mainhub.homebanking.repositories.ClientRepository;
//import com.mainhub.homebanking.services.implement.AccountServicesImpl;
//import com.mainhub.homebanking.utils.GenerateNumber;
//import com.mainhub.homebanking.utils.Validations;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//
//import java.util.Arrays;
//import java.util.Collections;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//class AccountServicesImplTest {
//
//    @Mock
//    private AccountRepository accountRepository;
//
//    @Mock
//    private ClientRepository clientRepository;
//
//    @Mock
//    private GenerateNumber generateNumber;
//
//    @Mock
//    private Validations validations;
//
//    @Mock
//    private Authentication authentication;
//
//    @InjectMocks
//    private AccountServicesImpl accountServices;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testCreateAccount() {
//        // Configurar el mock de Authentication
//        when(authentication.getName()).thenReturn("test@example.com");
//
//        // Configurar el mock del cliente
//        Client client = new Client();
//        when(clientRepository.findByEmail("test@example.com")).thenReturn(client);
//
//        // Configurar el mock del repositorio de cuentas para devolver una lista vacía
//        when(accountRepository.findByClient(client)).thenReturn(Collections.emptyList());
//
//        // Configurar el mock del generador de números
//        when(generateNumber.generateAccountNumber()).thenReturn("123456789");
//
//        // Ejecutar el método a probar
//        ResponseEntity<?> response = accountServices.createAccount(authentication);
//
//        // Verificar el resultado
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//        assertThat(response.getBody()).isEqualTo("Account created");
//
//        // Verificar que se haya guardado la cuenta
//        verify(accountRepository, times(1)).save(any(Account.class));
//        verify(clientRepository, times(1)).save(client);
//    }
//}
