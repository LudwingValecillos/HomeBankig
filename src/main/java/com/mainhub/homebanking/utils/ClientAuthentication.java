package com.mainhub.homebanking.utils;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class ClientAuthentication {
    @Autowired
    private ClientRepository clientRepository;

    public Client getClient(Authentication authentication) {
        return clientRepository.findByEmail(authentication.getName());
    }

//    public boolean validate(){
//
//    }
}
