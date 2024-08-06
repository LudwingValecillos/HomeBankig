package com.mainhub.homebanking.controllers;

import com.fasterxml.jackson.annotation.Nulls;
import com.mainhub.homebanking.Dtos.ClientDTO;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/clients")
//Se le dice api para seguir la convención de rutas para el controlador

// Indica que la clase es un controlador de recursos y maneja las solicitudes HTTP (GET, POST, PUT, DELETE, etc.).

// La ruta base para las solicitudes es "/api/clients".

public class ClientController {
    @Autowired
    //conecta/cablea a la interfaz de clientrepository que esta extiende de jpa repository para utilizar sus metodos, lo que se denomida inyeccion de dependencias.
    private ClientRepository clientRepository;
    // Inyección de dependencias para el repositorio de clientes.

    ////////------------------------------- Servlet --------------------------------------//////////

    //Mapping es lo que se representa en el navegador
    @GetMapping("/")
    // Maneja las solicitudes GET a la ruta base "/" para obtener todos los clientes.
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());

        // .stream() es una operación que devuelve un flujo de datos que puede ser consumido de forma eficiente.
    }

    @GetMapping("/id={id}")
    // Maneja las solicitudes GET para obtener un cliente por ID.
    public ResponseEntity<Client> getById(@PathVariable Long id) {
        //PathVariable es un parámetro de ruta que se pasa en la URL de la solicitud HTTP para que busque la entidad correspondiente
        return clientRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/id={id}")
    // Maneja las solicitudes DELETE para eliminar un cliente por ID.
    public void deleteClient(@PathVariable Long id) {
        clientRepository.deleteById(id);
    }

    @PostMapping("/create")
    // Maneja las solicitudes POST para crear un nuevo cliente.
    public Client agregarClient(@RequestParam String email, @RequestParam String firstName, @RequestParam String lastName) {
        return clientRepository.save(new Client(firstName, lastName, email));
    }

    @PutMapping("/update/{id}")
    // Maneja las solicitudes PUT para actualizar un cliente existente.
    public ResponseEntity<?> updateClient(@PathVariable Long id, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String email) {
        Client client = clientRepository.findById(id).orElse(null);

        if (client == null) {
            return new ResponseEntity<>("El hijo de remil puta no existe " + id, HttpStatus.NOT_FOUND);
        }

        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(email);
        Client updatedClient = clientRepository.save(client);
        return new ResponseEntity<>(updatedClient, HttpStatus.OK);
    }

    //ResponseEntity es una clase que representa una respuesta HTTP. Se utiliza para devolver una respuesta HTTP con un código de estado y un objeto de respuesta.
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> partialUpdateClient(@PathVariable Long id,
                                                 @RequestParam(required = false) String firstName,
                                                 @RequestParam(required = false) String lastName,
                                                 @RequestParam(required = false) String email) {

        Client client = clientRepository.findById(id).orElse(null);

        if (client == null) {
            return new ResponseEntity<>("El hijo de remil puta no existe " + id, HttpStatus.NOT_FOUND);
        }

        if (firstName != null) {
            client.setFirstName(firstName);
        }
        if (lastName != null) {
            client.setLastName(lastName);
        }
        if (email != null) {
            client.setEmail(email);
        }

        Client updatedClient = clientRepository.save(client);
        return new ResponseEntity<>(updatedClient, HttpStatus.OK);
    }


    @GetMapping("/hello")
    //
    public String getClients() {
        return "Hello Clientes, me gusta el pan";
    }
}
