package com.mainhub.homebanking.controllers;

import com.mainhub.homebanking.DTO.AccountDTO;
import com.mainhub.homebanking.DTO.ClientDTO;
import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.repositories.AccountRepository;
import com.mainhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

    @Autowired
    private AccountRepository accountRepository;
    static private int num = 7;
    // Inyección de dependencias para el repositorio de clientes.

    ////////------------------------------- Servlet --------------------------------------//////////

    //Mapping es lo que se representa en el navegador
    @GetMapping("/")
    // Maneja las solicitudes GET a la ruta base "/" para obtener todos los clientes.
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream().filter(client -> client.isActive()).map(client -> new ClientDTO(client)).collect(toList());

        // .stream() es una operación que devuelve un flujo de datos que puede ser consumido de forma eficiente.
        //.map(ClientDTO::new) es una operación que aplica una función a cada elemento del flujo de datos y devuelve un nuevo flujo de datos con los resultados.
        // .collect(toList()) es una operación que convierte el flujo de datos en una lista.

    }

    @GetMapping("/id={id}")
    // Maneja las solicitudes GET para obtener un cliente por ID.
    public ResponseEntity<ClientDTO> getById(@PathVariable Long id) {

        return clientRepository.findById(id).map(ClientDTO::new) // Convertir Client a ClientDTO
                .map(ResponseEntity::ok) // Si está presente, devolver 200 OK con el ClientDTO
                .orElse(ResponseEntity.notFound().build());

    }

    @GetMapping("/disabled")
    // Maneja las solicitudes GET para obtener todos los clientes desactivados.
    public List<ClientDTO> getAllClientsDisabled() {
        return clientRepository.findAll().stream().filter(client -> client.isActive() == false).map(client -> new ClientDTO(client)).collect(toList());

    }
    @DeleteMapping("/id={id}")
    // Maneja las solicitudes DELETE para eliminar un cliente por ID.
    public ResponseEntity<String> deleteClient(@PathVariable Long id) {
        Client client = clientRepository.findById(id).orElse(null);
        client.setActive(false);
        clientRepository.save(client);
        return ResponseEntity.ok("Client desactived");
    }

    @PostMapping("/create")
    // Maneja las solicitudes POST para crear un nuevo cliente.
    public ResponseEntity<ClientDTO> agregarClient(@RequestParam String email, @RequestParam String firstName, @RequestParam String lastName) {
        Client client = new Client(firstName, lastName, email);


        ClientDTO clientDTO = new ClientDTO(clientRepository.save(client));

//        String numero = "VIN00"+ String.valueOf(this.num);

        Account account = new Account( "VIN00"+ String.valueOf(this.num), LocalDate.now(), 0);
        this.num = this.num + 1;

        client.addAccount(account);
        AccountDTO accountDTO = new AccountDTO(accountRepository.save(account));


        return ResponseEntity.ok(clientDTO);
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

        ClientDTO clientDTO = new ClientDTO(clientRepository.save(client));


        return new ResponseEntity<>(clientDTO, HttpStatus.OK);
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


        ClientDTO clientDTO = new ClientDTO(clientRepository.save(client));

        return new ResponseEntity<>(clientDTO, HttpStatus.OK);
    }


    @GetMapping("/hello")
    //
    public String getClients() {
        return "Hello Clientes, me gusta el pan";
    }
}
