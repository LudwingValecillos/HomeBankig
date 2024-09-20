//package com.mainhub.homebanking.ServicesTest;
//
//import com.mainhub.homebanking.DTO.RegisterDTO;
//import com.mainhub.homebanking.models.Account;
//import com.mainhub.homebanking.models.Client;
//import com.mainhub.homebanking.repositories.AccountRepository;
//import com.mainhub.homebanking.repositories.ClientRepository;
//import com.mainhub.homebanking.services.implement.AuthServiceImpl;
//import com.mainhub.homebanking.utils.GenerateNumber;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
//class AuthServiceImplTest {
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @Mock
//    private ClientRepository clientRepository;
//
//    @Mock
//    private AccountRepository accountRepository;
//
////    @Mock
//    private GenerateNumber generateNumber;
//
//    @InjectMocks
//    private AuthServiceImpl authService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testRegister() {
//        // Configurar el mock del RegisterDTO
//        RegisterDTO registerDTO = new RegisterDTO("John", "Doe", "john.doe@example.com", "Password123!");
//
//        // Configurar el mock del ClientRepository para que devuelva null cuando se busca por email
//        when(clientRepository.findByEmail("john.doe@example.com")).thenReturn(null);
//
//        // Configurar el mock del PasswordEncoder para que devuelva una contraseña codificada
//        when(passwordEncoder.encode("Password123!")).thenReturn("encodedPassword");
//
//        // Configurar el mock del GenerateNumber para devolver un número de cuenta simulado
//        when(generateNumber.generateAccountNumber()).thenReturn("123456789");
//
//        // Ejecutar el método a probar
//        ResponseEntity<?> response = authService.register(registerDTO);
//
//        // Verificar el resultado
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//
//        // Verificar que se hayan guardado el cliente y la cuenta
//        verify(clientRepository, times(1)).save(any(Client.class));
//        verify(accountRepository, times(1)).save(any(Account.class));
//    }
//
//    @Test
//    void testRegisterWithExistingEmail() {
//        // Configurar el mock del RegisterDTO
//        RegisterDTO registerDTO = new RegisterDTO("John", "Doe", "john.doe@example.com", "Password123!");
//
//        // Configurar el mock del ClientRepository para que devuelva un cliente existente
//        when(clientRepository.findByEmail("john.doe@example.com")).thenReturn(new Client());
//
//        // Ejecutar el método a probar
//        ResponseEntity<?> response = authService.register(registerDTO);
//
//        // Verificar el resultado
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
//        assertThat(response.getBody()).isEqualTo("Email already exists");
//
//        // Verificar que no se hayan guardado el cliente ni la cuenta
//        verify(clientRepository, never()).save(any(Client.class));
//        verify(accountRepository, never()).save(any(Account.class));
//    }
//}
