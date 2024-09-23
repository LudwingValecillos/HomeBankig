package com.mainhub.homebanking;

import com.mainhub.homebanking.DTO.AccountDTO;
import com.mainhub.homebanking.DTO.ClientDTO;
import com.mainhub.homebanking.models.*;
import com.mainhub.homebanking.models.type.CardColor;
import com.mainhub.homebanking.models.type.CardType;
import com.mainhub.homebanking.models.type.TransactionType;
import com.mainhub.homebanking.utils.GenerateNumber;
import com.mainhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private GenerateNumber num;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean // Le dice a Spring que lo tenga en cuenta cuando arranca la aplicación
	public CommandLineRunner initData(ClientRepository clientRepository,
									  AccountRepository accountRepository,
									  TransactionRepository transactionRepository,
									  LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository,
									  CardRepository cardRepository) {
		return (args) -> {
			LocalDate today = LocalDate.now();
			LocalDate tomorrow = today.plusDays(1);

			// ***** Cliente Ludwing *****
			Client ludwing = new Client("Ludwing", "Valecillos", "ludwingval@gmail.com", passwordEncoder.encode("123"));
			clientRepository.save(ludwing);

			// Crear y guardar cuentas para Pepe
			Account cuenta1Pepe = new Account("VIN001", today, 500);
			Account cuenta2Pepe = new Account("VIN002", today, 7500);
			Account cuenta3Pepe = new Account("VIN003", today, 7500);

			ludwing.addAccount(cuenta1Pepe);
			ludwing.addAccount(cuenta2Pepe);
			ludwing.addAccount(cuenta3Pepe);

			accountRepository.save(cuenta1Pepe);
			accountRepository.save(cuenta2Pepe);
			accountRepository.save(cuenta3Pepe);

			// Crear y guardar transacciones para la cuenta 1 de Pepe
			Transaction transaccion1Pepe1 = new Transaction(TransactionType.CREDIT, 200, "Salary");
			Transaction transaccion2Pepe1 = new Transaction(TransactionType.DEBIT, -100, "Groceries");
			Transaction transaccion3Pepe1 = new Transaction(TransactionType.DEBIT, -50, "Utilities");
			cuenta1Pepe.addTransaction(transaccion1Pepe1);
			cuenta1Pepe.addTransaction(transaccion2Pepe1);
			cuenta1Pepe.addTransaction(transaccion3Pepe1);
			transactionRepository.save(transaccion1Pepe1);
			transactionRepository.save(transaccion2Pepe1);
			transactionRepository.save(transaccion3Pepe1);

			// Crear y guardar transacciones para la cuenta 2 de Pepe
			Transaction transaccion1Pepe2 = new Transaction(TransactionType.CREDIT, 300, "Bonus");
			Transaction transaccion2Pepe2 = new Transaction(TransactionType.DEBIT, -200, "Rent");
			Transaction transaccion3Pepe2 = new Transaction(TransactionType.DEBIT, -150, "Utilities");
			cuenta2Pepe.addTransaction(transaccion1Pepe2);
			cuenta2Pepe.addTransaction(transaccion2Pepe2);
			cuenta2Pepe.addTransaction(transaccion3Pepe2);
			transactionRepository.save(transaccion1Pepe2);
			transactionRepository.save(transaccion2Pepe2);
			transactionRepository.save(transaccion3Pepe2);

			// *****------------------------------ Cliente Melba --------------------------------*****
			Client melba = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("123"));
			clientRepository.save(melba);

			// Crear y guardar cuentas para Melba
			Account cuenta1Melba = new Account("VIN004", today, 10000);
			Account cuenta2Melba = new Account("VIN005", today, 2000);
//			Account cuenta3Melba = new Account("VIN010", today, 2000);

			melba.addAccount(cuenta1Melba);
			melba.addAccount(cuenta2Melba);
//			melba.addAccount(cuenta3Melba);
			accountRepository.save(cuenta1Melba);
			accountRepository.save(cuenta2Melba);
//			accountRepository.save(cuenta3Melba);


			// Crear y guardar transacciones para la cuenta 1 de Melba
			Transaction transaccion1Melba1 = new Transaction(TransactionType.CREDIT, 5000, "Freelance Work");
			Transaction transaccion2Melba1 = new Transaction(TransactionType.DEBIT, -2000, "New Laptop");
			Transaction transaccion3Melba1 = new Transaction(TransactionType.DEBIT, -100, "Groceries");
			cuenta1Melba.addTransaction(transaccion1Melba1);
			cuenta1Melba.addTransaction(transaccion2Melba1);
			cuenta1Melba.addTransaction(transaccion3Melba1);
			transactionRepository.save(transaccion1Melba1);
			transactionRepository.save(transaccion2Melba1);
			transactionRepository.save(transaccion3Melba1);

			// Crear y guardar transacciones para la cuenta 2 de Melba
			Transaction transaccion1Melba2 = new Transaction(TransactionType.CREDIT, 3000, "Consulting");
			Transaction transaccion2Melba2 = new Transaction(TransactionType.DEBIT, -1500, "Vacation");
			Transaction transaccion3Melba2 = new Transaction(TransactionType.DEBIT, -500, "Groceries");
			cuenta2Melba.addTransaction(transaccion1Melba2);
			cuenta2Melba.addTransaction(transaccion2Melba2);
			cuenta2Melba.addTransaction(transaccion3Melba2);
			transactionRepository.save(transaccion1Melba2);
			transactionRepository.save(transaccion2Melba2);
			transactionRepository.save(transaccion3Melba2);

			// Crear DTOs de clientes
			ClientDTO clientDTO1 = new ClientDTO(ludwing);
			ClientDTO clientDTO2 = new ClientDTO(melba);

			// Crear DTOs de cuentas
			AccountDTO accountDTO1 = new AccountDTO(cuenta1Pepe);
			AccountDTO accountDTO2 = new AccountDTO(cuenta2Pepe);
			AccountDTO accountDTO5 = new AccountDTO(cuenta3Pepe);
			AccountDTO accountDTO3 = new AccountDTO(cuenta1Melba);
			AccountDTO accountDTO4 = new AccountDTO(cuenta2Melba);
//			AccountDTO accountDTO6 = new AccountDTO(cuenta3Melba);



			Loan hipotcario = new Loan("Mortgage", 500000, Arrays.asList(12, 24, 36, 48, 60));
			loanRepository.save(hipotcario);

			Loan personal = new Loan("Personal", 100000, Arrays.asList(6, 12, 24));
			loanRepository.save(personal);

			Loan automotriz = new Loan("Automotive", 300000, Arrays.asList(6, 12, 24,36));
			loanRepository.save(automotriz);

			// --------------------------- LOAM------------------------
			//MELBA PIDIENDO PRESTAMO 😒
			ClientLoan clientLoan1 = new ClientLoan(400, 60);

			melba.addClientLoan(clientLoan1);

			hipotcario.addClientLoan(clientLoan1);

			clientLoanRepository.save(clientLoan1);

			ClientLoan clientLoan2 = new ClientLoan(50000, 12);

			melba.addClientLoan(clientLoan2);

			personal.addClientLoan(clientLoan2);

			clientLoanRepository.save(clientLoan2);

			//PEPE PIDIENDO PRESTAMO 😒
			ClientLoan clientLoan3 = new ClientLoan(100000, 24);

			ludwing.addClientLoan(clientLoan3);

			personal.addClientLoan(clientLoan3);

			clientLoanRepository.save(clientLoan3);

			ClientLoan clientLoan4 = new ClientLoan(200000, 36);

			ludwing.addClientLoan(clientLoan4);

			automotriz.addClientLoan(clientLoan4);

			clientLoanRepository.save(clientLoan4);

			//------------------CARDS----------------------

			Card cardLud = new Card(LocalDateTime.now().plusYears(5).plusYears(5), CardType.DEBIT, CardColor.SILVER);


			ludwing.addCard(cardLud);
			cardRepository.save(cardLud);

			Card card1 = new Card(LocalDateTime.now().plusYears(5), CardType.DEBIT, CardColor.GOLD);
			Card card2 = new Card(LocalDateTime.now().plusYears(10), CardType.CREDIT, CardColor.TITANIUM);

			melba.addCard(card1);
			melba.addCard(card2);
			cardRepository.save(card1);
			cardRepository.save(card2);


		};
	}
}
