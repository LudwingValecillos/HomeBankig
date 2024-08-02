package com.mainhub.homebanking.controllers;

import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @DeleteMapping("/id={id}")
    public void deleteClient(@PathVariable Long id) {
        clientRepository.deleteById(id);

    }
    @PutMapping("/id={id}&firstName={firstName}&lastName={lastName}&email={email}")
    public void updateClient(@PathVariable Long id, @PathVariable String firstName, @PathVariable String lastName, @PathVariable String email) {
        Optional<Client> clientOptional = clientRepository.findById(id);
        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            client.setFirstName(firstName);
            client.setLastName(lastName);
            client.setEmail(email);
            clientRepository.save(client);
        } else {
            System.out.println("El cliente no existe");
        }
    }
    @PutMapping("/id={id}&email={email}")
    public void putEmail(@PathVariable Long id, @PathVariable String email){
        Optional<Client> clientOptional = clientRepository.findById(id);

        if (clientOptional.isPresent()){
            Client client = clientOptional.get();
            client.setEmail(email);            clientRepository.save(client);
        }
    }
    @PostMapping("/email={email}&firstName={firstName}&lastName={lastName}")
    public void agregarClient(@PathVariable String email, @PathVariable String firstName, @PathVariable String lastName) {
        clientRepository.save(new Client(firstName, lastName, email));
    }


    @GetMapping("/hello")
    public String getClients() {
        return "Hello Clientes, me gusta el pan";
    }
}
