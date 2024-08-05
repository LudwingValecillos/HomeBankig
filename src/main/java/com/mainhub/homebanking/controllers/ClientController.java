package com.mainhub.homebanking.controllers;

import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController // Indica que la clase es un controlador de recursos, se encarga de manejar las solicitudes HTTP (GET, POST, PUT, DELETE, etc.)
@RequestMapping("/api/clients")// Indica la ruta base para las solicitudes. Api es una convención para los controladores de recursos
public class ClientController {
    @Autowired // Inyección de dependencias para el repositorio de clientes
    private ClientRepository clientRepository; // Inyección de dependencias para el repositorio de clientes, la implentacion lo hace hibernate

    @GetMapping("/")
    //  Indica que la ruta "/" es el punto de entrada para las solicitudes GET

    // Estamos estableciendo que vamos a recibir una peticion en este caso de tipo GET, por medio del controlador para acceder al metodo getAllClients
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }


    @GetMapping("/id={id}")
    public ResponseEntity<Client> getById(@PathVariable Long id) { // @PathVariable indica que el parámetro id es una variable de ruta (Path ruta en español)
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

    //Servlet es un objeto que se encarga de procesar las solicitudes HTTP.
    @PutMapping("/id={id}&email={email}")
    public void putEmail(@PathVariable Long id, @PathVariable String email){
        Optional<Client> clientOptional = clientRepository.findById(id);

        if (clientOptional.isPresent()){
            Client client = clientOptional.get();
            client.setEmail(email);
            clientRepository.save(client);
        }
    }
    @PostMapping("/create")
    public Client agregarClient(@RequestParam String email, @RequestParam String firstName, @RequestParam String lastName) {
        return clientRepository.save(new Client(firstName, lastName, email));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestParam Map<String, String> params) {
        Client client = clientRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found with id " + id));

        if (params.containsKey("firstName")) {
            client.setFirstName(params.get("firstName"));
        }
        if (params.containsKey("lastName")) {
            client.setLastName(params.get("lastName"));
        }
        if (params.containsKey("email")) {
            client.setEmail(params.get("email"));
        }

        Client updatedClient = clientRepository.save(client);
        return new ResponseEntity<>(updatedClient, HttpStatus.OK);
    }

    @GetMapping("/hello")
    public String getClients() {
        return "Hello Clientes, me gusta el pan";
    }
}
