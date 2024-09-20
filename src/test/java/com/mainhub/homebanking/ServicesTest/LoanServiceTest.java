//package com.mainhub.homebanking.ServicesTest;
//
//import com.mainhub.homebanking.DTO.ClientLoanDTO;
//import com.mainhub.homebanking.DTO.LoanAplicationDTO;
//import com.mainhub.homebanking.DTO.LoanDTO;
//import com.mainhub.homebanking.models.*;
//import com.mainhub.homebanking.models.type.TransactionType;
//import com.mainhub.homebanking.repositories.*;
//import com.mainhub.homebanking.services.implement.LoanServicesImpl;
//import com.mainhub.homebanking.utils.Validations;
//import net.bytebuddy.matcher.ElementMatcher;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static net.bytebuddy.matcher.ElementMatchers.is;
//import static org.aspectj.apache.bcel.Repository.instanceOf;
//import static org.hamcrest.Matchers.not;
//import static org.hamcrest.Matchers.nullValue;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//import static org.hamcrest.MatcherAssert.assertThat;
//
//
//@SpringBootTest
//class LoanServiceTest {
//
//    @Mock
//    private ClientRepository clientRepository;
//
//    @Mock
//    private LoanRepository loanRepository;
//
//    @Mock
//    private ClientLoanRepository clientLoanRepository;
//
//    @Mock
//    private AccountRepository accountRepository;
//
//    @Mock
//    private Validations validations;
//
//    @Mock
//    private TransactionRepository transactionRepository;
//
//    @InjectMocks
//    private LoanServicesImpl loanService;
//
//    @Test
//    void testGetAllLoansDTO() {
//        List<LoanDTO> loans = loanService.getAllLoansDTO();
//
//        assertEquals(3, loans.size());
//    }
//
//    @Test
//    void testGetClient() {
//        Client client = new Client("Melba", "Morel", "melba@mindhub.com", "password");
//        when(clientRepository.findByEmail("melba@mindhub.com")).thenReturn(client);
//
//        Client retrievedClient = loanService.getClient("melba@mindhub.com");
//        assertEquals("Melba", retrievedClient.getFirstName());
//    }
//
//    @Test
//    void testGetLoan() {
//        Loan loan = new Loan("Personal", 5000, Arrays.asList(6, 12, 24,36));
//        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
//
//        Loan retrievedLoan = loanService.getLoan(1L);
//        assertThat(retrievedLoan, not(nullValue()));
//    }
//
//    @Test
//    void testValidateLoanApplication() {
//        LoanAplicationDTO loanAplicationDTO = new LoanAplicationDTO(1L, 100, 12, "VIN001");
//
//        Client mockClient = new Client();
//        when(clientRepository.findByEmail("ludwingval@gmail.com")).thenReturn(mockClient);
//
//        Loan mockLoan = new Loan();
//        when(loanRepository.findById(1L)).thenReturn(Optional.of(mockLoan));
//
//        String validationResponse = loanService.validateLoanApplication(loanAplicationDTO, mockClient, mockLoan);
//
//        assertEquals("Loan already applied", validationResponse);
//    }
//}
//
