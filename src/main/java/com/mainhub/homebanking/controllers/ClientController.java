package com.mainhub.homebanking.controllers;

import com.mainhub.homebanking.DTO.AccountDTO;
import com.mainhub.homebanking.DTO.ClientDTO;
import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.repositories.AccountRepository;
import com.mainhub.homebanking.repositories.ClientRepository;
import com.mainhub.homebanking.services.ClientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientServices clientService;

    @GetMapping("/")
    public ResponseEntity<?> getAllClients() {
        return new ResponseEntity<>(clientService.getAllClientDto(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById(@PathVariable Long id) {
        if (clientService.getClientById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(clientService.getClientDto(clientService.getClientById(id)), HttpStatus.OK);
        }
    }
}


//    @GetMapping("/")
//    public List<ClientDTO> getAllClients() {
//        return clientRepository.findAll().stream().filter(client -> client.isActive()).map(client -> new ClientDTO(client)).collect(toList());
//    }
//    @GetMapping("/{id}")
//    // Maneja las solicitudes GET para obtener un cliente por ID.
//    public ResponseEntity<ClientDTO> getById(@PathVariable Long id) {
//
//        return clientRepository.findById(id).map(ClientDTO::new) // Convertir Client a ClientDTO
//                .map(ResponseEntity::ok) // Si está presente, devolver 200 OK con el ClientDTO
//                .orElse(ResponseEntity.notFound().build());
//
//    }
//
//    @GetMapping("/disabled")
//    // Maneja las solicitudes GET para obtener todos los clientes desactivados.
//    public List<ClientDTO> getAllClientsDisabled() {
//        return clientRepository.findAll().stream().filter(client -> client.isActive() == false).map(client -> new ClientDTO(client)).collect(toList());
//    }
//    @DeleteMapping("/{id}")
//    // Maneja las solicitudes DELETE para eliminar un cliente por ID.
//    public ResponseEntity<String> deleteClient(@PathVariable Long id) {
//        Client client = clientRepository.findById(id).orElse(null);
//        client.setActive(false);
//        clientRepository.save(client);
//        return ResponseEntity.ok("Client desactived");
//    }
//
//    @PutMapping("/update/{id}")
//    // Maneja las solicitudes PUT para actualizar un cliente existente.
//    public ResponseEntity<?> updateClient(@PathVariable Long id, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String email) {
//        Client client = clientRepository.findById(id).orElse(null);
//
//        if (client == null) {
//            return new ResponseEntity<>("El hijo de remil puta no existe " + id, HttpStatus.NOT_FOUND);
//        }
//
//        client.setFirstName(firstName);
//        client.setLastName(lastName);
//        client.setEmail(email);
//
//        ClientDTO clientDTO = new ClientDTO(clientRepository.save(client));
//
//
//        return new ResponseEntity<>(clientDTO, HttpStatus.OK);
//    }
//
//    //ResponseEntity es una clase que representa una respuesta HTTP. Se utiliza para devolver una respuesta HTTP con un código de estado y un objeto de respuesta.
//    @PatchMapping("/update/{id}")
//    public ResponseEntity<?> partialUpdateClient(@PathVariable Long id,
//                                                 @RequestParam(required = false) String firstName,
//                                                 @RequestParam(required = false) String lastName,
//                                                 @RequestParam(required = false) String email) {
//
//        Client client = clientRepository.findById(id).orElse(null);
//
//        if (client == null) {
//            return new ResponseEntity<>("El hijo de remil puta no existe " + id, HttpStatus.NOT_FOUND);
//        }
//
//        if (firstName != null) {
//            client.setFirstName(firstName);
//        }
//        if (lastName != null) {
//            client.setLastName(lastName);
//        }
//        if (email != null) {
//            client.setEmail(email);
//        }
//
//
//        ClientDTO clientDTO = new ClientDTO(clientRepository.save(client));
//
//        return new ResponseEntity<>(clientDTO, HttpStatus.OK);
//    }
//
//    @GetMapping("/test")
//    public ResponseEntity<?> test (Authentication authentication){
//        String email = authentication.getName();
//        return ResponseEntity.ok("Hello"+ email);



