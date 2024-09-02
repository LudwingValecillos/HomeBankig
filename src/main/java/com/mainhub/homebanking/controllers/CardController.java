package com.mainhub.homebanking.controllers;

import com.mainhub.homebanking.DTO.CardDTO;
import com.mainhub.homebanking.DTO.NewCardDTO;
import com.mainhub.homebanking.models.Card;
import com.mainhub.homebanking.models.Client;
import com.mainhub.homebanking.models.type.CardColor;
import com.mainhub.homebanking.models.type.CardType;
import com.mainhub.homebanking.repositories.CardRepository;
import com.mainhub.homebanking.repositories.ClientRepository;
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
    CardRepository cardRepository;

    @Autowired
    ClientRepository clientRepository;


    @GetMapping("/")
    public List<CardDTO> getCards(Authentication authentication) {
        return cardRepository.findAll().stream().map(CardDTO::new).collect(toList());
    }
    @PostMapping("/clients/current/cards")
    public ResponseEntity<?> createCard(Authentication authentication, @RequestBody NewCardDTO newCardDTO) {
//        LoggerFactory.getLogger(CardController.class).info("CREATE CARD");
        Client client = clientRepository.findByEmail(authentication.getName());

        if (client == null) {
            System.out.println("nei");
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }
        List<Card> cardsDebit = client.getCards().stream().filter(card -> card.getType() == CardType.DEBIT).toList();


        List<Card> cardsCredit = client.getCards().stream().filter(card -> card.getType() == CardType.CREDIT).toList();

        if(cardsDebit.size() >= 3 || cardsCredit.size() >= 3) {
            return new ResponseEntity<>("You can't have more than 3 cards", HttpStatus.FORBIDDEN);
        }

        CardColor color;
        try {
            color = CardColor.valueOf(newCardDTO.color().toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid color", HttpStatus.BAD_REQUEST);
        }

//        if (cardsDebit.stream().anyMatch(card -> card.getColor() == color) ||
//                cardsCredit.stream().anyMatch(card -> card.getColor() == color)) {
//            return new ResponseEntity<>("You already have a card with this color", HttpStatus.BAD_REQUEST);
//        }

        CardType type = newCardDTO.type().equalsIgnoreCase("DEBIT") ? CardType.DEBIT : CardType.CREDIT;

        Card card = new Card(LocalDateTime.now(), LocalDateTime.now().plusYears(5), type, color);

        client.addCard(card);

        cardRepository.save(card);


        return new ResponseEntity<>("Card created", HttpStatus.CREATED);

    }
}
