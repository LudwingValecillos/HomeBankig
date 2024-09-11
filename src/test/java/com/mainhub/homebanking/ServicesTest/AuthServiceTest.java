package com.mainhub.homebanking.ServicesTest;

import com.mainhub.homebanking.DTO.LoginDTO;
import com.mainhub.homebanking.DTO.RegisterDTO;
import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.repositories.AccountRepository;
import com.mainhub.homebanking.repositories.ClientRepository;
import com.mainhub.homebanking.services.implement.AuthServiceImpl;
import com.mainhub.homebanking.servicesSecurity.JwtUtilService;
import com.mainhub.homebanking.utils.GenerateNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtUtilService jwtUtilService;

    @Mock
    private GenerateNumber num;

    @InjectMocks
    private AuthServiceImpl authService;

    private Client client;
    private Account account;
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        client = new Client("John", "Doe", "test@example.com", "password123");
        account = new Account("VIN001", LocalDate.now(), 0);
        authentication = mock(Authentication.class);
    }

    @Test
    public void testLogin_Success() {
        LoginDTO loginDTO = new LoginDTO("test@example.com", "password123");
        when(clientRepository.findByEmail(loginDTO.email())).thenReturn(client);
        when(userDetailsService.loadUserByUsername(loginDTO.email())).thenReturn(mock(UserDetails.class));
        when(jwtUtilService.generateToken(any(UserDetails.class))).thenReturn("token");

        ResponseEntity<?> response = authService.login(loginDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authenticationManager).authenticate(any());
    }

    @Test
    public void testLogin_InvalidCredentials() {
        LoginDTO loginDTO = new LoginDTO("test@example.com", "wrongpassword");

        doThrow(new RuntimeException()).when(authenticationManager).authenticate(any());

        ResponseEntity<?> response = authService.login(loginDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testRegister_Success() {
        RegisterDTO registerDTO = new RegisterDTO("John", "Doe", "test@example.com", "Password123!");
        when(clientRepository.findByEmail(registerDTO.email())).thenReturn(null);
        when(passwordEncoder.encode(registerDTO.password())).thenReturn("encodedPassword");

        ResponseEntity<?> response = authService.register(registerDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(clientRepository).save(any(Client.class));
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    public void testRegister_EmailAlreadyExists() {
        RegisterDTO registerDTO = new RegisterDTO("John", "Doe", "test@example.com", "Password123!");
        when(clientRepository.findByEmail(registerDTO.email())).thenReturn(client);

        ResponseEntity<?> response = authService.register(registerDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testValidateLogin_EmptyEmailOrPassword() {
        LoginDTO loginDTO = new LoginDTO("", "");

        String result = authService.validateLogin(loginDTO);

        assertEquals("Email or password invalid", result);
    }

    @Test
    public void testAuthenticate_Success() {
        LoginDTO loginDTO = new LoginDTO("test@example.com", "password123");

        authService.authenticate(loginDTO);

        verify(authenticationManager).authenticate(any());
    }

    @Test
    public void testGenerateToken() {
        UserDetails userDetails = mock(UserDetails.class);
        when(jwtUtilService.generateToken(userDetails)).thenReturn("token");

        String token = authService.generateToken(userDetails);

        assertEquals("token", token);
    }

    @Test
    public void testGetUserDetailsService() {
        LoginDTO loginDTO = new LoginDTO("test@example.com", "password123");
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(loginDTO.email())).thenReturn(userDetails);

        UserDetails result = authService.getUserDetailsService(loginDTO);

        assertEquals(userDetails, result);
    }

    @Test
    public void testGenerateAccount() {
        when(num.generateAccountNumber()).thenReturn("VIN002");

        Account result = authService.generateAccount(new RegisterDTO("John", "Doe", "test@example.com", "Password123!"));

        assertEquals("VIN002", result.getNumber());
        assertEquals(0, result.getBalance());
    }

    @Test
    public void testGenerateClient() {
        RegisterDTO registerDTO = new RegisterDTO("John", "Doe", "test@example.com", "Password123!");
        when(passwordEncoder.encode(registerDTO.password())).thenReturn("encodedPassword");

        Client result = authService.generateClient(registerDTO);

        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
    }

    @Test
    public void testSaveClientAndAccount() {
        authService.saveClientAndAccount(client, account);

        verify(clientRepository).save(client);
        verify(accountRepository).save(account);
    }

}
