package com.mainhub.homebanking.controllers;

import com.mainhub.homebanking.DTO.CardDTO;
import com.mainhub.homebanking.DTO.NewCardDTO;
import com.mainhub.homebanking.models.Card;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.models.type.CardColor;
import com.mainhub.homebanking.models.type.CardType;
import com.mainhub.homebanking.repositories.CardRepository;
import com.mainhub.homebanking.repositories.ClientRepository;
import com.mainhub.homebanking.services.CardServices;
import org.apache.catalina.filters.RemoteIpFilter;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/cards/")
public class CardController {


    @Autowired
    CardServices cardServices;


    @GetMapping("/")
    public List<CardDTO> getCards(Authentication authentication) {
        return cardServices.getAllCardsDTO();
    }

    @GetMapping("/clients/current/cards")
    public List<CardDTO> getAllCards(Authentication authentication) {
        return cardServices.getAllCardsDTO();
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<?> createCard(Authentication authentication, @RequestBody NewCardDTO newCardDTO) {
        return cardServices.createCard(authentication, newCardDTO);

    }
}
