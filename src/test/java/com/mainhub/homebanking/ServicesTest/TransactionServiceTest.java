//package com.mainhub.homebanking.ServicesTest;
//
//import com.mainhub.homebanking.DTO.NewTransactionDTO;
//import com.mainhub.homebanking.models.Account;
//import com.mainhub.homebanking.models.Client;
//import com.mainhub.homebanking.models.Transaction;
//import com.mainhub.homebanking.models.type.TransactionType;
//import com.mainhub.homebanking.repositories.AccountRepository;
//import com.mainhub.homebanking.repositories.ClientRepository;
//import com.mainhub.homebanking.repositories.TransactionRepository;
//import com.mainhub.homebanking.services.TransactionsService;
//import com.mainhub.homebanking.services.implement.TransactionServiceImpl;
//import org.hamcrest.MatcherAssert;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
//import static org.hamcrest.Matchers.*;
//import static org.hamcrest.Matchers.empty;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.*;
//
//@SpringBootTest
//public class TransactionServiceTest {
//
//    @Autowired
//    private ClientRepository clientRepository;
//
//    @Autowired
//    private AccountRepository accountRepository;
//
//    @Autowired
//    private TransactionRepository transactionRepository;
//
//    @Autowired
//    private TransactionsService transactionService;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testValidateTransaction() {
//        NewTransactionDTO transactionDTO = new NewTransactionDTO(0, "test description", "sourceAccount", "destinationAccount");
//
//        String validationResponse = transactionService.validateTransaction(transactionDTO);
//        assertEquals("The 'amount' field is required and cannot be zero.", validationResponse);
//
//        transactionDTO = new NewTransactionDTO(100, "", "sourceAccount", "destinationAccount");
//        validationResponse = transactionService.validateTransaction(transactionDTO);
//        assertEquals("The 'description' field is required.", validationResponse);
//
//        transactionDTO = new NewTransactionDTO(100, "test description", "", "destinationAccount");
//        validationResponse = transactionService.validateTransaction(transactionDTO);
//        assertEquals("The 'source account' field is required.", validationResponse);
//
//        transactionDTO = new NewTransactionDTO(100, "test description", "sourceAccount", "");
//        validationResponse = transactionService.validateTransaction(transactionDTO);
//        assertEquals("The 'destination account' field is required.", validationResponse);
//
//        transactionDTO = new NewTransactionDTO(100, "test description", "sameAccount", "sameAccount");
//        validationResponse = transactionService.validateTransaction(transactionDTO);
//        assertEquals("Source and destination accounts cannot be the same", validationResponse);
//    }
//
//
//    @Test
//    void testGetClient() {
//       Client client = transactionService.getClient("melba@mindhub.com");
//
//        assertThat(client, notNullValue());
//    }
//
//    @Test
//    public void testValidateAmount() {
//
//        NewTransactionDTO transactionDTO = new NewTransactionDTO(100, "test description", "VIN001", "VIN004");
//
//
//        String validationResponse = transactionService.validateAmount(transactionDTO);
//
//
//        assertThat(validationResponse, is(nullValue()));
//
//    }
//
//    @Test
//    public void testPerformTransaction() {
//        Account sourceAccount = new Account();
//        Account destinationAccount = new Account();
//
//
//        NewTransactionDTO transactionDTO = new NewTransactionDTO(100, "test description", "VIN001", "VIN004");
//
//        String result = transactionService.performTransaction(transactionDTO);
//        assertEquals("Transaction created", result);
//
//
//    }
//
//    @Test
//    public void testProcessTransaction() {
//        NewTransactionDTO transactionDTO = new NewTransactionDTO(100, "test description", "VIN001", "VIN004");
//        ResponseEntity<?> response = transactionService.processTransaction("ludwingval@gmail.com", transactionDTO);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Transaction created", response.getBody());
//    }
//}
