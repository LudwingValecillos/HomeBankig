package com.mainhub.homebanking.servicesSecurity;

import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Repositorio para acceder a los datos de los clientes.
    @Autowired
    private ClientRepository clientRepository;

    /**
     * Carga los detalles del usuario basado en el nombre de usuario (correo electrónico en este caso).
     *
     * @param username El correo electrónico del cliente.
     * @return Un objeto UserDetails con la información del cliente.
     * @throws UsernameNotFoundException Si no se encuentra un cliente con el correo proporcionado.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca el cliente en el repositorio usando el correo electrónico.
        Client client = clientRepository.findByEmail(username);

        // Lanza una excepción si no se encuentra el cliente.
        if (client == null) {
            throw new UsernameNotFoundException(username);
        }

        // Asigna rol ADMIN si el correo electrónico contiene "admin".
        if (username.contains("admin")) {
            return User.withUsername(username)
                    .password(client.getPassword())
                    .roles("ADMIN")
                    .build();
        }

        // Crea y retorna un objeto UserDetails para clientes normales.
        return User.withUsername(username)
                .password(client.getPassword())
                .roles("CLIENT")
                .build();
    }
}
