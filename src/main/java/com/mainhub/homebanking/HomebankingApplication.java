package com.mainhub.homebanking;

import com.mainhub.homebanking.Dtos.AccountDTO;
import com.mainhub.homebanking.Dtos.ClientDTO;
import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.models.Transaction;
import com.mainhub.homebanking.models.TransactionType;
import com.mainhub.homebanking.repositories.AccountRepository;
import com.mainhub.homebanking.repositories.ClientRepository;
import com.mainhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean // Le dice a Spring que lo tenga en cuenta cuando arranca la aplicación
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
		return (args) -> {
			LocalDate today = LocalDate.now();
			LocalDate tomorrow = today.plusDays(1);

			// ***** Cliente Pepe *****
			Client pepe = new Client("Pepe", "Pepe", "pepe@mindhub.com");
			clientRepository.save(pepe);

			// Crear y guardar cuentas para Pepe
			Account cuenta1Pepe = new Account("VIN001", today, 500);
			Account cuenta2Pepe = new Account("VIN002", today, 7500);
			pepe.addAccount(cuenta1Pepe);
			pepe.addAccount(cuenta2Pepe);
			accountRepository.save(cuenta1Pepe);
			accountRepository.save(cuenta2Pepe);

			// Crear y guardar transacciones para la cuenta 1 de Pepe
			Transaction transaccion1Pepe1 = new Transaction(TransactionType.CREDIT, 200, "Salary", LocalDateTime.now());
			Transaction transaccion2Pepe1 = new Transaction(TransactionType.DEBIT, -100, "Groceries", LocalDateTime.now());
			Transaction transaccion3Pepe1 = new Transaction(TransactionType.DEBIT, -50, "Utilities", LocalDateTime.now());
			cuenta1Pepe.addTransaction(transaccion1Pepe1);
			cuenta1Pepe.addTransaction(transaccion2Pepe1);
			cuenta1Pepe.addTransaction(transaccion3Pepe1);
			transactionRepository.save(transaccion1Pepe1);
			transactionRepository.save(transaccion2Pepe1);
			transactionRepository.save(transaccion3Pepe1);

			// Crear y guardar transacciones para la cuenta 2 de Pepe
			Transaction transaccion1Pepe2 = new Transaction(TransactionType.CREDIT, 300, "Bonus", LocalDateTime.now());
			Transaction transaccion2Pepe2 = new Transaction(TransactionType.DEBIT, -200, "Rent", LocalDateTime.now());
			Transaction transaccion3Pepe2 = new Transaction(TransactionType.DEBIT, -150, "Utilities", LocalDateTime.now());
			cuenta2Pepe.addTransaction(transaccion1Pepe2);
			cuenta2Pepe.addTransaction(transaccion2Pepe2);
			cuenta2Pepe.addTransaction(transaccion3Pepe2);
			transactionRepository.save(transaccion1Pepe2);
			transactionRepository.save(transaccion2Pepe2);
			transactionRepository.save(transaccion3Pepe2);

			// *****------------------------------ Cliente Melba --------------------------------*****
			Client melba = new Client("Melba", "Morel", "melba@mindhub.com");
			clientRepository.save(melba);

			// Crear y guardar cuentas para Melba
			Account cuenta1Melba = new Account("VIN003", today, 10000);
			Account cuenta2Melba = new Account("VIN004", today, 2000);
			melba.addAccount(cuenta1Melba);
			melba.addAccount(cuenta2Melba);
			accountRepository.save(cuenta1Melba);
			accountRepository.save(cuenta2Melba);

			// Crear y guardar transacciones para la cuenta 1 de Melba
			Transaction transaccion1Melba1 = new Transaction(TransactionType.CREDIT, 5000, "Freelance Work", LocalDateTime.now());
			Transaction transaccion2Melba1 = new Transaction(TransactionType.DEBIT, -2000, "New Laptop", LocalDateTime.now());
			Transaction transaccion3Melba1 = new Transaction(TransactionType.DEBIT, -100, "Groceries", LocalDateTime.now());
			cuenta1Melba.addTransaction(transaccion1Melba1);
			cuenta1Melba.addTransaction(transaccion2Melba1);
			cuenta1Melba.addTransaction(transaccion3Melba1);
			transactionRepository.save(transaccion1Melba1);
			transactionRepository.save(transaccion2Melba1);
			transactionRepository.save(transaccion3Melba1);

			// Crear y guardar transacciones para la cuenta 2 de Melba
			Transaction transaccion1Melba2 = new Transaction(TransactionType.CREDIT, 3000, "Consulting", LocalDateTime.now());
			Transaction transaccion2Melba2 = new Transaction(TransactionType.DEBIT, -1500, "Vacation", LocalDateTime.now());
			Transaction transaccion3Melba2 = new Transaction(TransactionType.DEBIT, -500, "Groceries", LocalDateTime.now());
			cuenta2Melba.addTransaction(transaccion1Melba2);
			cuenta2Melba.addTransaction(transaccion2Melba2);
			cuenta2Melba.addTransaction(transaccion3Melba2);
			transactionRepository.save(transaccion1Melba2);
			transactionRepository.save(transaccion2Melba2);
			transactionRepository.save(transaccion3Melba2);

			// Crear DTOs de clientes
			ClientDTO clientDTO1 = new ClientDTO(pepe);
			ClientDTO clientDTO2 = new ClientDTO(melba);
			// ClientDTO clientDTO3 = new ClientDTO(chloe);

			// Crear DTOs de cuentas
			AccountDTO accountDTO1 = new AccountDTO(cuenta1Pepe);
			AccountDTO accountDTO2 = new AccountDTO(cuenta2Pepe);
			AccountDTO accountDTO3 = new AccountDTO(cuenta1Melba);
			AccountDTO accountDTO4 = new AccountDTO(cuenta2Melba);
			// AccountDTO accountDTO5 = new AccountDTO(cuenta1Chloe);
			// AccountDTO accountDTO6 = new AccountDTO(cuenta2Chloe);

			// Los DTOs pueden ser utilizados para otras operaciones,
			// como enviar datos a la vista o realizar otras transformaciones.
		};
	}
}
