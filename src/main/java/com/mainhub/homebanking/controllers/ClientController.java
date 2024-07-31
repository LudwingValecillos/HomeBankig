package com.mainhub.homebanking.controllers;

import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/")
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }


@GetMapping("/id={id}")
public ResponseEntity<Client> getById(@PathVariable Long id) {
    Optional<Client> clientOptional = clientRepository.findById(id);
    System.out.println(clientRepository);
    if (clientOptional.isPresent()) {
        Client client = clientOptional.get();
        client.setId(id); // Asegúrate de establecer el ID del cliente en el objeto
        return ResponseEntity.ok(client);
    } else {
        return ResponseEntity.notFound().build();
    }

}


    @GetMapping("/hello")
    public String getClients() {
        return "Hello Clientes, me gusta el pan";
    }
}
