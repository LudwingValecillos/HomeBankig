package com.mainhub.homebanking.controllers;

import com.mainhub.homebanking.DTO.AccountDTO;
import com.mainhub.homebanking.DTO.ClientDTO;
import com.mainhub.homebanking.DTO.LoginDTO;
import com.mainhub.homebanking.DTO.RegisterDTO;
import com.mainhub.homebanking.models.Account;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.repositories.AccountRepository;
import com.mainhub.homebanking.repositories.ClientRepository;
import com.mainhub.homebanking.servicesSecurity.JwtUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    static private int num = 007;

    // Endpoint para iniciar sesión y generar un token JWT.
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {

            //authenticate es validar las credenciales. Spring Security se encargará de realizar esta validación,
            // por ejemplo, consultando una base de datos de usuarios para verificar si el email y la contraseña coinciden con un usuario registrado.

            /**
             Cuando un usuario intenta iniciar sesión (endpoint /login), se crea un objeto Authentication con las credenciales proporcionadas (email y contraseña).
             Este objeto se pasa al AuthenticationManager para su validación.
             Si las credenciales son válidas, se crea un nuevo objeto Authentication que representa al usuario
             autenticado y se establece en el contexto de seguridad de Spring Security.
             */
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password()));


            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.email());


            final String jwt = jwtUtilService.generateToken(userDetails);


            // Retorna el token JWT en la respuesta.
            return ResponseEntity.ok(jwt);
        } catch (Exception e) {

            e.printStackTrace(); // Muestra cualquier excepción que ocurra.
            // Retorna un error si la autenticación falla.
            return new ResponseEntity<>("Email or password invalid", HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint para registrar un nuevo cliente.
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {


        if (clientRepository.findByEmail(registerDTO.email()) != null) {
            return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
        }

        // Verifica si el nombre y apellido no están vacíos.
        if (registerDTO.firstName().isBlank() || registerDTO.lastName().isBlank()) {
            return new ResponseEntity<>("First name and last name cannot be empty", HttpStatus.BAD_REQUEST);
        }

        // Verifica si la contraseña cumple con el requisito mínimo de longitud.
        if (registerDTO.password().length() < 8) {
            return new ResponseEntity<>("Password must be at least 8 characters long", HttpStatus.BAD_REQUEST);
        }

        // Codifica la contraseña antes de almacenarla.
//        String encodedPassword = passwordEncoder.encode(registerDTO.password());

        // Crea un nuevo cliente con la información proporcionada.
        Client newClient = new Client(registerDTO.firstName(), registerDTO.lastName(), registerDTO.email(), passwordEncoder.encode(registerDTO.password()));

        // Guarda el cliente y convierte a DTO.
        ClientDTO clientDTO = new ClientDTO(clientRepository.save(newClient));

        // Crea una nueva cuenta para el cliente.
        Account account = new Account("VIN-" + String.valueOf(this.num), LocalDate.now(), 0);
        this.num += 1;

        // Asocia la cuenta al cliente.
        newClient.addAccount(account);
        // Guarda la cuenta y convierte a DTO.
        AccountDTO accountDTO = new AccountDTO(accountRepository.save(account));

        // Guarda el nuevo cliente en la base de datos.
        clientRepository.save(newClient);

        // Retorna una respuesta exitosa con un mensaje de confirmación.
        return new ResponseEntity<>("Client registered successfully", HttpStatus.CREATED);
    }

    //El objeto Authentication proporciona información sobre el usuario actualmente autenticado, como su nombre de usuario, roles, y otros atributos.
    @GetMapping("/current")
    public ResponseEntity<?> getClient(Authentication authentication) {

        // Obtiene el cliente basado en el nombre de usuario autenticado.
        Client client = clientRepository.findByEmail(authentication.getName());

        // Retorna los detalles del cliente en la respuesta.
        return ResponseEntity.ok(new ClientDTO(client));
    }
}
