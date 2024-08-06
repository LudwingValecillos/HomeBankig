package com.mainhub.homebanking;

import com.mainhub.homebanking.Dtos.AccountDTO;
import com.mainhub.homebanking.Dtos.ClientDTO;
import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.repositories.AccountRepository;
import com.mainhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean // Le dice a Spring que lo tenga en cuenta cuando arranca la aplicación
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository) {
		return (args) -> {
			LocalDate today = LocalDate.now();
			LocalDate tomorrow = today.plusDays(1);

			// Crear clientes
			Client pepe = new Client("Pepe", "Pepe", "pepe@mindhub.com");
			Client melba = new Client("Melba", "Morel", "melba@mindhub.com");
			Client chloe = new Client("Chloe", "O'Brian", "chloe@example.com");

			// Crear DTOs de clientes
			ClientDTO clientDTO = new ClientDTO(pepe);
			ClientDTO clientDTO2 = new ClientDTO(melba);
			ClientDTO clientDTO3 = new ClientDTO(chloe);

			// Guardar clientes en la base de datos
			clientRepository.save(pepe);
			clientRepository.save(melba);
			clientRepository.save(chloe);

			// Crear y guardar cuentas para Pepe
			Account cuenta1Pepe = new Account("VIN001", today, 500);

			AccountDTO accountDTO = new AccountDTO(cuenta1Pepe);

			Account cuenta2Pepe = new Account("VIN002", today, 7500);

			AccountDTO accountDTO2 = new AccountDTO(cuenta2Pepe);

			pepe.addAccount(cuenta1Pepe);
			pepe.addAccount(cuenta2Pepe);
			accountRepository.save(cuenta1Pepe);
			accountRepository.save(cuenta2Pepe);

			// Crear y guardar cuentas para Melba
			Account cuenta1Melba = new Account("VIN003", today, 10000);

			AccountDTO accountDTO3 = new AccountDTO(cuenta1Melba);

			Account cuenta2Melba = new Account("VIN004", today, 2000);

			AccountDTO accountDTO4 = new AccountDTO(cuenta2Melba);

			melba.addAccount(cuenta1Melba);
			melba.addAccount(cuenta2Melba);
			accountRepository.save(cuenta1Melba);
			accountRepository.save(cuenta2Melba);

			// Crear y guardar cuentas para Chloe
			Account cuenta1Chloe = new Account("VIN005", tomorrow, 0);
			AccountDTO accountDTO5 = new AccountDTO(cuenta1Chloe);

			Account cuenta2Chloe = new Account("VIN006", tomorrow, 0);

			AccountDTO accountDTO6 = new AccountDTO(cuenta2Chloe);

			chloe.addAccount(cuenta1Chloe);
			chloe.addAccount(cuenta2Chloe);
			accountRepository.save(cuenta1Chloe);
			accountRepository.save(cuenta2Chloe);

			// Los DTOs (clientDTO, accountDTO, etc.) pueden ser utilizados para otras operaciones,
			// como enviar datos a la vista o realizar otras transformaciones.
		};
	}
}
