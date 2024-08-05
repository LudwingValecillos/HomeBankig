package com.mainhub.homebanking;

import com.mainhub.homebanking.models.Client;
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

	@Bean // Inyección de dependencias para el repositorio de clientes Bean es un objeto que se inyecta en el controlador.

	public CommandLineRunner initData(ClientRepository clientRepository) { // CommandLineRunner es una interfaz que define un método de inicialización de línea de comandos.
		
		return (args) -> { // El método de inicialización de línea de comandos se ejecuta cuando se inicia la aplicación. args es un arreglo de argumentos de línea de comandos.
			// Guardar algunos clientes
			Client pepe = new Client("Pepe", "Pepe", "pepe@mindhub.com");

			clientRepository.save(pepe);

			clientRepository.save(new Client("Melba", "Morel", "melba@mindhub.com"));
			clientRepository.save(new Client("Chloe", "O'Brian", "chloe@example.com"));
			clientRepository.save(new Client("Kim", "Bauer", "kim@example.com"));
			clientRepository.save(new Client("David", "Palmer", "david@example.com"));
			clientRepository.save(new Client("Sol", "Osorio", "osorio@example.com"));

			clientRepository.save(new Client("Michelle", "Dessler", "michelle@example.com"));
		};
	}
}