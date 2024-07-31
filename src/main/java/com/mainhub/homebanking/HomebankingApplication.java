package com.mainhub.homebanking;

import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.models.TypePet;
import com.mainhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository) {
		
		return (args) -> {
			// Guardar algunos clientes
			Client pepe = new Client("Pepe", "Pepe", "pepe@mindhub.com");


			clientRepository.save(pepe);


			clientRepository.save(new Client("Melba", "Morel", "melba@mindhub.com"));
			clientRepository.save(new Client("Chloe", "O'Brian", "chloe@example.com"));
			clientRepository.save(new Client("Kim", "Bauer", "kim@example.com"));
			clientRepository.save(new Client("David", "Palmer", "david@example.com"));
			clientRepository.save(new Client("Michelle", "Dessler", "michelle@example.com"));
		};
	}
}