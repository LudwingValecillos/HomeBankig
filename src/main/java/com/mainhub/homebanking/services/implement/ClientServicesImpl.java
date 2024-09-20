package com.mainhub.homebanking.services.implement;

import com.mainhub.homebanking.DTO.ClientDTO;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.repositories.ClientRepository;
import com.mainhub.homebanking.services.ClientServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServicesImpl implements ClientServices {

        @Autowired
        public ClientRepository clientRepository;

        @Override
        public List<Client> getAllClient() {
            return clientRepository.findAll();
        }

        @Override
        public List<ClientDTO> getAllClientDto() {
            return getAllClient().stream().map(ClientDTO::new).collect(Collectors.toList());
        }

        @Override
        public Client getClientById(Long id) {
            return clientRepository.findById(id).orElse(null);
        }

        @Override
        public ClientDTO getClientDto(Client client) {
            return new ClientDTO(client);
        }

}
